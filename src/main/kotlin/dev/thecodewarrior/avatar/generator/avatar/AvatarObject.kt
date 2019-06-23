package dev.thecodewarrior.avatar.generator.avatar

import dev.thecodewarrior.avatar.SvgRoot
import dev.thecodewarrior.avatar.dsl.Path
import dev.thecodewarrior.avatar.generator.SvgObject
import dev.thecodewarrior.avatar.util.Vec2d
import dev.thecodewarrior.avatar.util.intersectLines
import dev.thecodewarrior.avatar.util.path
import dev.thecodewarrior.avatar.util.vec
import org.redundent.kotlin.xml.Node
import org.redundent.kotlin.xml.xml
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class AvatarObject(val root: SvgRoot): SvgObject() {
    var eventHorizonStyle: Map<String, Any?> = mapOf("fill" to "#000")
    var haloStyle: Map<String, Any?> = mapOf("fill" to "#fff")
    var accretionDiskStyle: Map<String, Any?> = mapOf("fill" to "#fff")

    var halo = true
    var accretionDisk = true
    var jets = false
    var tiltZ = Math.toRadians(-45.0)
    var tiltX = Math.toRadians(-13.0)

    var eventHorizonRadius = 30
    var haloRadius = 40
    var accretionDiskRadius = 90

    override fun generate(): Node = xml("g") {
        attributes(
            "id" to "avatar",
            "transform" to "rotate(${Math.toDegrees(-tiltZ)})"
        )

        if (halo) {
            "circle" {
                attributes(
                    "id" to "halo",
                    "cx" to 0,
                    "cy" to 0,
                    "r" to haloRadius
                )
                attributes.putAll(haloStyle)
            }
        }

        "circle" {
            attributes(
                "id" to "event-horizon-back",
                "cx" to 0,
                "cy" to 0,
                "r" to eventHorizonRadius
            )
            attributes.putAll(eventHorizonStyle)
        }

        if (accretionDisk) {
            "ellipse" {
                attributes(
                    "id" to "accretion-disk",
                    "cx" to 0,
                    "cy" to 0,
                    "rx" to accretionDiskRadius,
                    "ry" to accretionDiskRadius * abs(root.tiltRatio)
                )
                attributes.putAll(accretionDiskStyle)
            }
            "path" {
                attributes(
                    "id" to "event-horizon-front",
                    "d" to path {
                        move(vec(-eventHorizonRadius, 0))
                        arc(1, 1, 0, false, root.tiltRatio > 0, vec(eventHorizonRadius, 0))
                        arc(1, root.tiltRatio, 0, false, root.tiltRatio > 0, vec(-eventHorizonRadius, 0))
                    }
                )
                attributes.putAll(eventHorizonStyle)
            }
        }

        if (jets) {
            addNode(generateJets())
        }
    }

    var jetBaseSize = 0.5
    var jetWidth = 10.0
    var jetLength = 1500.0
    var jetSpread = 0.0
    var jetStartDistance = 20.0
    var jetExitAngle = 0.0
    var jetStyle: Map<String, Any?> = mapOf("fill" to "#fff")

    private fun generateJets(): Node = xml("g") {
        val yFactor = cos(tiltX)
        val sliceY = sqrt(1 - jetBaseSize * jetBaseSize)
        val ellipseY = eventHorizonRadius * sliceY * yFactor

        run { // ellipse base
            val rx = eventHorizonRadius * jetBaseSize
            val ry = rx * abs(sin(tiltX))
            "ellipse" {
                attributes(
                    "id" to "jet-top-base",
                    "rx" to rx,
                    "ry" to ry,
                    "cx" to 0,
                    "cy" to ellipseY
                )
                attributes.putAll(jetStyle)
            }
        }

        run { // tilted beam points
            var curveEnd = vec(jetWidth / 2, jetStartDistance)
            var curveStart = vec(eventHorizonRadius * jetBaseSize, 0)

            var tangentAngle = asin(jetBaseSize)
            val maxAngle = acos((curveStart - curveEnd).normalize().x)
            tangentAngle += (maxAngle - tangentAngle) * jetExitAngle
            val tangent = vec(-cos(tangentAngle), sin(tangentAngle))

            var jetEnd = curveEnd + vec(sin(jetSpread), cos(jetSpread)) * jetLength

            var controlPoint = intersectLines(curveStart, tangent, curveEnd, (curveEnd-jetEnd).normalize()) ?: return@xml

            fun transform(pos: Vec2d): Vec2d = pos * vec(1, yFactor) + vec(0, ellipseY)
            curveEnd = transform(curveEnd)
            curveStart = transform(curveStart)
            jetEnd = transform(jetEnd)
            controlPoint = transform(controlPoint)

            run { // ellipse neck
                val rx = curveEnd.x
                val ry = rx * abs(sin(tiltX))
                "ellipse" {
                    attributes(
                        "id" to "jet-top-neck",
                        "rx" to rx,
                        "ry" to ry,
                        "cx" to 0,
                        "cy" to curveEnd.y
                    )
                    attributes.putAll(jetStyle)
                }
            }

            run { // ellipse neck
                val rx = controlPoint.x
                val ry = rx * abs(sin(tiltX))
                "ellipse" {
                    attributes(
                        "id" to "jet-top-control",
                        "rx" to rx,
                        "ry" to ry,
                        "cx" to 0,
                        "cy" to controlPoint.y
                    )
                    attributes.putAll(jetStyle)
                }
            }

            val topPath = path {
                move(jetEnd)
                line(curveEnd)
                line(controlPoint)
                line(curveStart)
                arc(1, abs(sin(tiltX)), 0, false, true, curveStart.flipX())
                line(controlPoint.flipX())
                line(curveEnd.flipX())
                line(jetEnd.flipX())
                closePath()
            }

            "path" {
                attributes(
                    "id" to "jet-top",
                    "d" to topPath
                )
                attributes.putAll(jetStyle)
            }

            curveEnd = curveEnd.flipY()
            curveStart = curveStart.flipY()
            jetEnd = jetEnd.flipY()
            controlPoint = controlPoint.flipY()

            val bottomPath = path {
                move(jetEnd)
                line(curveEnd)
                line(controlPoint)
                line(curveStart)
                arc(1, abs(sin(tiltX)), 0, false, true, curveStart.flipX())
                line(controlPoint.flipX())
                line(curveEnd.flipX())
                line(jetEnd.flipX())
                closePath()
            }

            "path" {
                attributes(
                    "id" to "jet-bottom",
                    "d" to bottomPath
                )
                attributes.putAll(jetStyle)
            }
        }


    }

    private fun generateTopJetBase(): Pair<Double, Node> {
        val circleX = findContactX(jetBaseSize, root.tiltRatio)
        val rx = eventHorizonRadius * jetBaseSize
        val ry = rx * root.tiltRatio
        val ellipseX = circleX / jetBaseSize
        val circleY = sqrt(1 - circleX * circleX)
        val ellipseY = sqrt(1 - ellipseX * ellipseX)

        val circleContactY = circleY * eventHorizonRadius
        val ellipseContactY = ellipseY * ry

        return acos(circleX) to xml("ellipse"){
            attributes(
                "cx" to 0,
                "cy" to ellipseContactY-circleContactY,
                "rx" to rx,
                "ry" to ry,
                "fill" to "#fff"
            )
        }
    }


    /**
     * @param rx The X radius of the inner ellipse. Must be in the range (0, 1)
     * @param ry The Y radius of the inner ellipse. Must be in the range (0, [rx])
     * @return The x coordinate of the point where the specified ellipse first contacts the unit circle.
     */
    private fun findContactX(rx: Double, ry: Double): Double {
        // circle radius = 1, rx = x radius, ry = y radius
        //
        //# cx = resolved circle x = x
        // ^ one-to-one mapping on the circle function
        //# cm = circle slope = cx / sqrt(1 - cx^2)
        //
        //# ex = resolved ellipse x = x / rx
        // ^ if rx == 0.5, an x of 0.5 corresponds to 1.0 in the circle function
        //# em = ellipse slope = ex / sqrt(1 - ex^2) * ry
        //
        //# solve cm = em
        //# solve (x / sqrt(1 - x^2)) = (x / sqrt(1 - (x/rx)^2) * ry/rx) for x
        //# solve (x / sqrt(1 - x^2)) = (x / sqrt(1 - (x/w)^2) * h/w) for x
        // ^ reduced to single-variable names for Wolfram Alpha. rx -> w, ry -> h
        //
        // Wolfram Alpha's result:
        //#       sqrt(h^2 - w^4)
        //# x = ± –––––––––––––––
        //#       sqrt(h^2 - w^2)
        //
        // translated back to our names:
        //#       sqrt(ry^2 - rx^4)
        //# x = ± –––––––––––––––––
        //#       sqrt(ry^2 - rx^2)
        //
        // but if ry < rx, ry^2 < rx^2, ry^2 - rx^2 < 0
        //
        // Rephrased with the condition that h < w
        //# solve (x / sqrt(1 - x^2)) = (x / sqrt(1 - (x/rx)^2) * ry/rx) for x where ry < rx
        //# solve (x / sqrt(1 - x^2)) = (x / sqrt(1 - (x/w)^2) * h/w) for x where h < w
        //
        // Wolfram Alpha's result:
        //#            h^2 - w^2
        //# x = ± sqrt(–––––––––) and 0 < w < 1 and 0 < h < w
        //#             h^2 - 1
        //
        //#            ry^2 - rx^2
        //# x = ± sqrt(–––––––––––) and 0 < rx < 1 and 0 < ry < rx
        //#              ry^2 - 1
        //
        assert(0 < rx && rx < 1)
        assert(0 < ry && ry < rx)
        // Success!
        return sqrt( 0 +
            (ry*ry - rx*rx) /
            (ry*ry - 1)
        )
    }
}