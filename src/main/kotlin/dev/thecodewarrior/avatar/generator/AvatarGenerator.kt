package dev.thecodewarrior.avatar.generator

import dev.thecodewarrior.avatar.SvgRoot
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
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class AvatarGenerator(override val root: SvgRoot): SvgGeneratorBase {
    var halo = true
    var accretionDisk = true
    var jets = false
    var tilt = Math.toRadians(-45.0)

    var eventHorizonRadius = 30
    var haloRadius = 40
    var accretionDiskRadius = 30

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
                    "r" to haloRadius.p,
                    "fill" to "#f00"
                )
            }
        }

        "circle" {
            attributes(
                "id" to "event-horizon-back",
                "cx" to 0,
                "cy" to 0,
                "r" to eventHorizonRadius.p,
                "fill" to "#000"
            )
        }

        if (accretionDisk) {
            "ellipse" {
                attributes(
                    "id" to "accretion-disk",
                    "cx" to 0,
                    "cy" to 0,
                    "rx" to accretionDiskRadius.p,
                    "ry" to (accretionDiskRadius * root.tiltRatio).p,
                    "fill" to "#fff"
                )
            }
            "path" {
                attributes(
                    "id" to "event-horizon-front",
                    "fill" to "#000",
                    "d" to path {
                        move(vec(-eventHorizonRadius, 0).p)
                        arc(1, 1, 0, false, true, vec(eventHorizonRadius, 0).p)
                        arc(1, root.tiltRatio, 0, false, true, vec(-eventHorizonRadius, 0).p)
                    }
                )
            }
        }

        run {

//            "path" {
//                attributes(
//                    "fill" to "none",
//                    "stroke" to "#fff",
//                    "stroke-width" to 0.005,
//                    "d" to path {
//                        move(-vec(circleX, circleY) * eventHorizonRadius + vec(2, 0))
//                        rel().line(vec(-2, 0))
//                        abs().line(vec(-ellipseX*rx, ellipseContactY-circleContactY))
//                        rel().line(vec(-2, 0))
//                    }
//                )
//            }

        }

        if (jets) {
            addNode(generateJets())
        }
    }

    var jetBaseSize = 0.5
    var jetWidth = 10.0
    var jetLength = 1500.0
    var jetSpread = 0.001
    var jetStartDistance = 60.0
    var jetExitAngle = 0.0

    private fun generateJets(): Node = xml("g") {

//        val (jetTangentAngle, baseNode) = generateTopJetBase()
        val jetTangentAngle = acos(findContactX(jetBaseSize, root.tiltRatio))
//        addNode(baseNode)

        val jetStart = vec(jetWidth/2, jetStartDistance).flipY()
        val jetEnd = vec(jetWidth/2 + jetLength * jetSpread, eventHorizonRadius * jetLength).flipY()
        val jetTangent = (jetEnd - jetStart).normalize()

        val tangentPoint = vec(eventHorizonRadius*cos(jetTangentAngle), eventHorizonRadius*sin(jetTangentAngle)).flipY()
        val exitToStartAngle = acos((jetStart-tangentPoint).normalize().x)
        var exitAngle = jetTangentAngle + PI/2
        exitAngle += (exitToStartAngle-exitAngle)*jetExitAngle
        val tangent = vec(cos(exitAngle), sin(exitAngle)).flipY()

        val controlPoint = intersectLines(tangentPoint, tangent, jetStart, jetTangent) ?: return@xml

        val topPath = path {
//            move(jetEnd.p)
//            line(jetStart.p)
//            quad(controlPoint.p, tangentPoint.p)
//            line(tangentPoint.flipX().p)
            move(tangentPoint.p)
            val rx = eventHorizonRadius * jetBaseSize
            val ry = rx * root.tiltRatio
            arc(rx.p, ry.p, 0, true, true, tangentPoint.flipX().p)
            closePath()
//            quad(controlPoint.flipX().p, jetStart.flipX().p)
//            line(jetEnd.flipX().p)
//            closePath()
        }

        "path" {
            attributes(
                "id" to "jet-top",
                "d" to topPath,
                "fill" to "#fff"
//                "stroke" to "#f00",
//                "stroke-width" to 0.05
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