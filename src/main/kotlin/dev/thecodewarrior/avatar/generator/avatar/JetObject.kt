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
        var startDistance = 20.0
        var exitAngle = 0.0
        var style: Map<String, Any?> = mapOf("fill" to "#fff")
        var debugStyle: Map<String, Any?> = mapOf("fill" to "none")
    }

    val yFactor = cos(config.tiltX)
    val zFactor = sin(config.tiltX)
    val sliceY = sqrt(1 - config.baseSize * config.baseSize)
    val ellipseY = avatar.eventHorizonRadius * sliceY * yFactor
    val baseRadius = avatar.eventHorizonRadius * config.baseSize

    val endRadiusX = config.width / 2
    val endRadiusY = endRadiusX * zFactor
    val startRadiusX = baseRadius
    val startRadiusY = startRadiusX * zFactor

    var curveEnd: Vec2d
    var curveStart: Vec2d
    var controlPoint: Vec2d

    init {
        curveEnd = vec(config.width / 2, config.startDistance)
        curveStart = vec(baseRadius, 0)

        var tangentAngle = asin(config.baseSize)
        val maxAngle = acos((curveStart - curveEnd).normalize().x)
        tangentAngle += (maxAngle - tangentAngle) * config.exitAngle
        val tangent = vec(-cos(tangentAngle), sin(tangentAngle))

        var jetEnd = curveEnd + vec(sin(config.spread), cos(config.spread)) * config.length

        controlPoint = intersectLines(curveStart, tangent, curveEnd, (curveEnd-jetEnd).normalize())!!

        val yScale = vec(1, yFactor)
        controlPoint *= yScale
        curveEnd *= yScale
        jetEnd *= yScale
        controlPoint *= yScale
    }

    private fun getCurve(angle: Double): Curve {
        @Suppress("NAME_SHADOWING")
        val angle = angle - PI/2

        return Curve(
            start = vec(
                cos(angle) * baseRadius,
                sin(angle) * zFactor * baseRadius
            ),
            control = vec(
                cos(angle) * controlPoint.x,
                controlPoint.y + sin(angle) * zFactor * controlPoint.x
            ),
            end = vec(
                cos(angle) * curveEnd.x,
                curveEnd.y + sin(angle) * zFactor * curveEnd.x
            )
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

    fun generateJets(): Node = xml("g") {
        attributes(
            "id" to "jets",
            "transform" to "rotate(${Math.toDegrees(-(config.tiltZ))})"
        )

        "g"("transform" to "translate(0 $ellipseY)") {
            // tilted beam points

            val divisions = 4
            val step = PI / divisions
            var lastCurve = getCurve(0.0)
            (0 .. divisions).forEach {
                val angle = it * step
                val curve = getCurve(angle)
                "path" {
                    attributes(
                        "d" to path {
                            move(lastCurve.start)
                            cubic(lastCurve.cubicStart, lastCurve.cubicEnd, lastCurve.end)
                            arc(endRadiusX, endRadiusY, 0, false, true, curve.end)
                            cubic(curve.cubicEnd, curve.cubicStart, curve.start)
                            arc(startRadiusX, startRadiusY, 0, false, false, lastCurve.start)
                            closePath()
                        }
                    )
                    attributes.putAll(config.debugStyle)
                    attributes["fill"] = "#${distinctColor(it).copy(alpha = 127).hex}"
                }

                "path" {
                    attributes(
                        "d" to path {
                            move(lastCurve.start.flipX())
                            cubic(lastCurve.cubicStart.flipX(), lastCurve.cubicEnd.flipX(), lastCurve.end.flipX())
                            arc(endRadiusX, endRadiusY, 0, false, false, curve.end.flipX())
                            cubic(curve.cubicEnd.flipX(), curve.cubicStart.flipX(), curve.start.flipX())
                            arc(startRadiusX, startRadiusY, 0, false, true, lastCurve.start.flipX())
                            closePath()
                        }
                    )
                    attributes.putAll(config.debugStyle)
                    attributes["fill"] = "#${distinctColor(-it).copy(alpha = 127).hex}"
                }
                lastCurve = curve
            }
        }
    }
}