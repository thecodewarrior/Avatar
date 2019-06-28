package dev.thecodewarrior.avatar.generator.avatar

import dev.thecodewarrior.avatar.util.Vec2d
import dev.thecodewarrior.avatar.util.copy
import dev.thecodewarrior.avatar.util.distinctColor
import dev.thecodewarrior.avatar.util.hex
import dev.thecodewarrior.avatar.util.intersectLines
import dev.thecodewarrior.avatar.util.path
import dev.thecodewarrior.avatar.util.vec
import org.redundent.kotlin.xml.Node
import org.redundent.kotlin.xml.xml
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class JetObject(val avatar: AvatarObject, val config: Configuration) {
    class Configuration {
        var tiltZ = 0.0
        var tiltX = Math.toRadians(-13.0)
        var baseSize = 0.5
        var width = 10.0
        var length = 1500.0
        var spread = 0.0
        var startDistance = 40.0
        var exitAngle = 0.0
        var style: Map<String, Any?> = mapOf("fill" to "#fff")
        var debugColors: Boolean = false
    }

    val yFactor = cos(config.tiltX)
    val topZFactor = sin(config.tiltX)
    val bottomZFactor = -topZFactor
    var zFactor = topZFactor

    val sliceY = sqrt(1 - config.baseSize * config.baseSize)
    val ellipseY = avatar.eventHorizonRadius * sliceY * yFactor
    val baseRadius = avatar.eventHorizonRadius * config.baseSize

    val endRadiusX = config.width / 2
    val endRadiusY = endRadiusX * abs(zFactor)
    val startRadiusX = baseRadius - avatar.root.pointsPerPixel
    val startRadiusY = startRadiusX * abs(zFactor) - avatar.root.pointsPerPixel

    var curveEnd: Vec2d
    var curveStart: Vec2d
    var controlPoint: Vec2d
    var jetEnd: Vec2d

    init {
        curveEnd = vec(config.width / 2, config.startDistance)
        curveStart = vec(startRadiusX, 0)

        var tangentAngle = asin(config.baseSize)
        val maxAngle = acos((curveStart - curveEnd).normalize().x)
        tangentAngle += (maxAngle - tangentAngle) * config.exitAngle
        val tangent = vec(-cos(tangentAngle), sin(tangentAngle))

        jetEnd = curveEnd + vec(0, config.length)

        controlPoint = intersectLines(curveStart, tangent, curveEnd, (curveEnd-jetEnd).normalize())!!

        val yScale = vec(1, yFactor)
        controlPoint *= yScale
        curveEnd *= yScale
        jetEnd *= yScale
        controlPoint *= yScale
    }

    private fun getCurve(angle: Double, flipY: Boolean): Curve {
        @Suppress("NAME_SHADOWING")
        val angle = angle - PI/2

        return Curve(
            start = vec(
                cos(angle) * startRadiusX,
                sin(angle) * zFactor * startRadiusX
            ).let { if(flipY) it.flipY() else it },
            control = vec(
                cos(angle) * controlPoint.x,
                controlPoint.y + sin(angle) * zFactor * controlPoint.x
            ).let { if(flipY) it.flipY() else it },
            end = vec(
                cos(angle) * curveEnd.x,
                curveEnd.y + sin(angle) * zFactor * curveEnd.x
            ).let { if(flipY) it.flipY() else it }
        )
    }

    class Curve(val start: Vec2d, val control: Vec2d, val end: Vec2d) {
        /**
         * For some reason quadratic beziers aren't symmetrical (they look different when drawn start -> end vs.
         * end -> start), while cubic beziers are.
         *
         * Based on: https://stackoverflow.com/a/55034115/1541907
         */
        val cubicStart = start + (control - start) * (2.0/3)
        val cubicEnd = end + (control - end) * (2.0/3)
    }

    fun generateJets(top: Boolean): Node = xml("g") {
        attributes(
            "id" to "jets",
            "transform" to "rotate(${Math.toDegrees(-(config.tiltZ))})"
        )

        "g"("transform" to "translate(0 ${if(top) ellipseY else -ellipseY})") {
            zFactor = if(top) topZFactor else bottomZFactor
            generateJet(this, top)
        }
    }

    private fun generateJet(node: Node, top: Boolean) = node.apply {

        val jetEnd = if(!top) jetEnd.flipY() else jetEnd
        "path" {
            attributes(
                "d" to path {
                    move(jetEnd)
                    line(vec(jetEnd.x, 0))
                    arc(endRadiusX, endRadiusY, 0, false, config.tiltX < 0, vec(-curveEnd.x, 0))
                    line(jetEnd.flipX())
                    closePath()
                }
            )
            attributes.putAll(config.style)
        }

        val divisions = 50
        val backOffset = 0.0 //if(top) PI else 0.0
        val step = PI / divisions
        val tinyAngle = step / 2
        (0 .. divisions).forEach {
            val lastCurve = getCurve(it * step + backOffset - tinyAngle, !top)
            val curve = getCurve((it + 1) * step + backOffset + tinyAngle, !top)
            "path" {
                attributes(
                    "d" to path {
                        move(lastCurve.start)
                        cubic(lastCurve.cubicStart, lastCurve.cubicEnd, lastCurve.end)
                        arc(endRadiusX, endRadiusY, 0, false, top, curve.end)
                        cubic(curve.cubicEnd, curve.cubicStart, curve.start)
                        arc(startRadiusX, startRadiusY, 0, false, !top, lastCurve.start)
                        closePath()
                    }
                )
                attributes.putAll(config.style)
                if(config.debugColors)
                    attributes["fill"] = "#${distinctColor(it).copy(alpha = 127).hex}"
            }

            "path" {
                attributes(
                    "d" to path {
                        move(lastCurve.start.flipX())
                        cubic(lastCurve.cubicStart.flipX(), lastCurve.cubicEnd.flipX(), lastCurve.end.flipX())
                        arc(endRadiusX, endRadiusY, 0, false, !top, curve.end.flipX())
                        cubic(curve.cubicEnd.flipX(), curve.cubicStart.flipX(), curve.start.flipX())
                        arc(startRadiusX, startRadiusY, 0, false, top, lastCurve.start.flipX())
                        closePath()
                    }
                )
                attributes.putAll(config.style)
                if(config.debugColors)
                    attributes["fill"] = "#${distinctColor(-it).copy(alpha = 127).hex}"
            }
        }

        if(config.tiltX > 0 == top) {
            "ellipse"(
                "id" to "event-horizon-jetmask",
                "cx" to 0,
                "cy" to 0,
                "rx" to baseRadius,
                "ry" to baseRadius * abs(zFactor)
            ) {
                attributes.putAll(avatar.eventHorizonStyle)
            }
        }
    }
}