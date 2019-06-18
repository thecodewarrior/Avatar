package dev.thecodewarrior.avatar.generator

import dev.thecodewarrior.avatar.SvgRoot
import dev.thecodewarrior.avatar.dsl.Path
import dev.thecodewarrior.avatar.util.intersectLines
import dev.thecodewarrior.avatar.util.path
import dev.thecodewarrior.avatar.util.vec
import org.redundent.kotlin.xml.Node
import org.redundent.kotlin.xml.xml
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class AvatarGenerator(val root: SvgRoot): SvgGenerator {
    var halo = true
    var accretionDisk = true
    var jets = false
    var tilt = Math.toRadians(-45.0)

    var eventHorizonRadius = 30
    var haloRadius = 40
    var accretionDiskRadius = 90

    override fun generate(): Node = xml("g") {
        attributes(
            "id" to "avatar",
            "transform" to "rotate(${Math.toDegrees(tilt)})"
        )

        if (halo) {
            "circle" {
                attributes(
                    "id" to "halo",
                    "cx" to 0,
                    "cy" to 0,
                    "r" to haloRadius,
                    "fill" to "#fff"
                )
            }
        }

        "circle" {
            attributes(
                "id" to "event-horizon-back",
                "cx" to 0,
                "cy" to 0,
                "r" to eventHorizonRadius,
                "fill" to "#000"
            )
        }

        if (accretionDisk) {
            "ellipse" {
                attributes(
                    "id" to "accretion-disk",
                    "cx" to 0,
                    "cy" to 0,
                    "rx" to accretionDiskRadius,
                    "ry" to accretionDiskRadius * abs(root.tiltRatio),
                    "fill" to "#fff"
                )
            }
            "path" {
                attributes(
                    "id" to "event-horizon-front",
                    "fill" to "#000",
                    "d" to path {
                        move(vec(-eventHorizonRadius, 0))
                        arc(1, 1, 0, false, root.tiltRatio > 0, vec(eventHorizonRadius, 0))
                        arc(1, root.tiltRatio, 0, false, root.tiltRatio > 0, vec(-eventHorizonRadius, 0))
                    }
                )
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

    private fun generateJets(): Node = xml("g") {
        var path = Path()

        val contactX = findContactX(jetBaseSize, abs(root.tiltRatio))
        val contactAngle = acos(contactX)
        val contactPoint = vec(eventHorizonRadius * cos(contactAngle), eventHorizonRadius * sin(contactAngle)).flipY()
        // the center of the ellipse
        val ellipseY = contactPoint.y + sin(acos(contactX / jetBaseSize)) * jetBaseSize * eventHorizonRadius * abs(root.tiltRatio)

        run { // ellipse base
            val rx = eventHorizonRadius * jetBaseSize
            val ry = rx * abs(root.tiltRatio)
            "ellipse" {
                attributes(
                    "id" to "jet-top-base",
                    "rx" to rx,
                    "ry" to ry,
                    "cx" to 0,
                    "cy" to ellipseY,
                    "fill" to "#fff"
                )
            }
        }

        run { // tilted beam points
            var curveEnd = vec(jetWidth / 2, ellipseY - jetStartDistance)
            var curveStart = vec(eventHorizonRadius * jetBaseSize, ellipseY)

            var tangentAngle = acos(jetBaseSize)
            val diff = (curveEnd-curveStart).normalize()
            val maxAngle = acos(diff.x)
            tangentAngle += (maxAngle - tangentAngle) * jetExitAngle
            val tangent = vec(cos(tangentAngle), sin(tangentAngle))

            var jetEnd = curveEnd + vec(sin(jetSpread), cos(jetSpread)) * -jetLength

            var controlPoint = intersectLines(curveStart, tangent, curveEnd, (curveEnd-jetEnd).normalize()) ?: return@xml

            curveStart = (curveStart - vec(0, ellipseY)) * vec(1, root.tiltRatio) + vec(0, ellipseY)
            curveEnd = (curveEnd - vec(0, ellipseY)) * vec(1, root.tiltRatio) + vec(0, ellipseY)
            controlPoint = (controlPoint - vec(0, ellipseY)) * vec(1, root.tiltRatio) + vec(0, ellipseY)
            jetEnd = (jetEnd - vec(0, ellipseY)) * vec(1, root.tiltRatio) + vec(0, ellipseY)

            val topPath = path {
                move(curveStart)
                quad(controlPoint, curveEnd)
                line(jetEnd)
                line(jetEnd.flipX())
                line(curveEnd.flipX())
                quad(controlPoint.flipX(), curveStart.flipX())
                closePath()
            }

            "path" {
                attributes(
                    "id" to "jet-top",
                    "d" to topPath,
                    "fill" to "#fff"
                )
            }

            val bottomPath = path {
//                move(jetEnd.flipY())
//                line(jetStart.flipY())
//                quad(controlPoint.flipY(), tangentPoint.flipY())
//                val rx = eventHorizonRadius * jetBaseSize
//                val ry = rx * root.tiltRatio
//                arc(rx, ry, 0, root.tiltRatio < 0, root.tiltRatio > 0, tangentPoint.flipXY())
//                quad(controlPoint.flipXY(), jetStart.flipXY())
//                line(jetEnd.flipXY())
//                closePath()
            }

//            "path" {
//                attributes(
//                    "id" to "jet-bottom",
//                    "d" to bottomPath,
//                    "fill" to "#fff"
//                )
//            }
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