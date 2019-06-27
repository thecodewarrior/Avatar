package dev.thecodewarrior.avatar.generator.avatar

import dev.thecodewarrior.avatar.util.distinctColor
import dev.thecodewarrior.avatar.util.hex
import dev.thecodewarrior.avatar.util.intersectLines
import dev.thecodewarrior.avatar.util.path
import dev.thecodewarrior.avatar.util.vec
import org.redundent.kotlin.xml.Node
import org.redundent.kotlin.xml.xml
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class JetObject(val avatar: AvatarObject) {
    val config = Configuration()

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

    fun generateJets(): Node = xml("g") {
        attributes(
            "id" to "jets",
            "transform" to "rotate(${Math.toDegrees(-(config.tiltZ))})"
        )

        val yFactor = cos(config.tiltX)
        val zFactor = sin(config.tiltX)
        val sliceY = sqrt(1 - config.baseSize * config.baseSize)
        val ellipseY = avatar.eventHorizonRadius * sliceY * yFactor
        val baseRadius = (avatar.eventHorizonRadius * config.baseSize)

//        run { // ellipse base
//            val rx = baseRadius
//            val ry = rx * abs(zFactor)
//            "ellipse" {
//                attributes(
//                    "id" to "jet-top-base",
//                    "rx" to rx,
//                    "ry" to ry,
//                    "cx" to 0,
//                    "cy" to ellipseY
//                )
//                attributes.putAll(config.style)
//            }
//        }

        run { // tilted beam points
            var curveEnd = vec(config.width / 2, config.startDistance)
            var curveStart = vec(baseRadius, 0)

            var tangentAngle = asin(config.baseSize)
            val maxAngle = acos((curveStart - curveEnd).normalize().x)
            tangentAngle += (maxAngle - tangentAngle) * config.exitAngle
            val tangent = vec(-cos(tangentAngle), sin(tangentAngle))

            var jetEnd = curveEnd + vec(sin(config.spread), cos(config.spread)) * config.length

            var controlPoint = intersectLines(curveStart, tangent, curveEnd, (curveEnd-jetEnd).normalize()) ?: return@xml

            val yScale = vec(1, yFactor)
            controlPoint *= yScale
            curveEnd *= yScale
            jetEnd *= yScale
            controlPoint *= yScale

            val rx = baseRadius-controlPoint.x
            val h = controlPoint.y/(rx*zFactor)
            val angle = asin(1/h)

            var curveStartBack = vec(
                cos(angle) * baseRadius,
                sin(angle) * zFactor * baseRadius
            )
            var controlPointBack = vec(
                cos(angle) * controlPoint.x,
                controlPoint.y + sin(angle) * zFactor * controlPoint.x
            )
            var curveEndBack = vec(
                cos(angle) * curveEnd.x,
                curveEnd.y + sin(angle) * zFactor * curveEnd.x
            )

            val ellipsePos = vec(0, ellipseY)
            jetEnd += ellipsePos
            curveEnd += ellipsePos
            curveStart += ellipsePos
            controlPoint += ellipsePos
            curveEndBack += ellipsePos
            curveStartBack += ellipsePos
            controlPointBack += ellipsePos

            run { // debug
                run {
                    val rx = baseRadius
                    val ry = rx * abs(zFactor)
                    "ellipse" {
                        attributes(
                            "rx" to rx,
                            "ry" to ry,
                            "cx" to 0,
                            "cy" to ellipseY
                        )
                        attributes.putAll(config.debugStyle)
                    }
                }

                run {
                    val rx = curveEnd.x
                    val ry = rx * abs(zFactor)
                    "ellipse" {
                        attributes(
                            "rx" to rx,
                            "ry" to ry,
                            "cx" to 0,
                            "cy" to curveEnd.y
                        )
                        attributes.putAll(config.debugStyle)
                    }
                }

                run {
                    val rx = controlPoint.x
                    val ry = rx * abs(zFactor)
                    "ellipse" {
                        attributes(
                            "rx" to rx,
                            "ry" to ry,
                            "cx" to 0,
                            "cy" to controlPoint.y
                        )
                        attributes.putAll(config.debugStyle)
                    }
                }

                ((0 until 360 step 5).map { it.toDouble() } + listOf(Math.toDegrees(angle))).forEach {
                    val angle = Math.toRadians(it)
                    var curveStart = vec(
                        cos(angle) * baseRadius,
                        sin(angle) * zFactor * baseRadius + ellipseY
                    )
                    var controlPoint = vec(
                        cos(angle) * controlPoint.x,
                        controlPoint.y + sin(angle) * zFactor * controlPoint.x
                    )
                    var curveEnd = vec(
                        cos(angle) * curveEnd.x,
                        curveEnd.y + sin(angle) * zFactor * curveEnd.x
                    )
                    "path" {
                        attributes(
                            "d" to path {

                                //                                move(curveStart)
//                                line(controlPoint)
//                                move(curveStart)
//                                line(curveEnd)

                                move(curveStart)
                                quad(controlPoint, curveEnd)
                                move(vec(0, curveEnd.y))
                                closePath()
                            }
                        )
                        attributes.putAll(config.debugStyle)
                        attributes["stroke"] = "#${distinctColor(it.toInt()).hex}"
                    }
                }
//                "path" {
//                    attributes(
//                        "d" to path {
//                            move(jetEnd)
//                            line(curveEndBack)
//                            quad(controlPointBack, curveStartBack)
//                            line(curveStartBack.flipX())
//                            quad(controlPointBack.flipX(), curveEndBack.flipX())
//                            line(jetEnd.flipX())
//                            closePath()
//                        }
//                    )
//                    attributes.putAll(config.debugStyle)
//                }
            }


            "path" {
                attributes(
                    "id" to "jet-top",
                    "d" to path {
                        //                        move(jetEnd)
                        move(curveEnd)
                        quad(controlPoint, curveStart)
//                        line(curveStart.flipX())
                        arc(baseRadius, baseRadius * abs(zFactor), 0, config.tiltX < 0, config.tiltX < 0, curveStart.flipX())
                        quad(controlPoint.flipX(), curveEnd.flipX())
//                        line(jetEnd.flipX())
                        closePath()
                    }
                )
                attributes.putAll(config.style)
            }
            "path" {
                attributes(
                    "id" to "jet-top-back",
                    "d" to path {
                        //                        move(jetEnd)
                        move(curveEndBack)
                        quad(controlPointBack, curveStartBack)
//                        line(curveStartBack.flipX())
                        arc(baseRadius, baseRadius * abs(zFactor), 0, config.tiltX < 0, config.tiltX < 0, curveStartBack.flipX())
                        quad(controlPointBack.flipX(), curveEndBack.flipX())
//                        line(jetEnd.flipX())
                        closePath()
                    }
                )
                attributes.putAll(config.style)
            }

//            run { // ellipse base
//                val rx = baseRadius
//                val ry = rx * abs(zFactor)
//                "ellipse" {
//                    attributes(
//                        "id" to "jet-top-mask",
//                        "rx" to rx,
//                        "ry" to ry,
//                        "cx" to 0,
//                        "cy" to ellipseY
//                    )
//                    if(config.tiltX < 0)
//                        attributes.putAll(config.style)
//                    else
//                        attributes.putAll(eventHorizonStyle)
//                }
//            }



            jetEnd = jetEnd.flipY()
            curveEnd = curveEnd.flipY()
            curveStart = curveStart.flipY()
            controlPoint = controlPoint.flipY()
            curveEndBack = curveEndBack.flipY()
            curveStartBack = curveStartBack.flipY()
            controlPointBack = controlPointBack.flipY()

            "defs" { // ellipse base
                "mask"(
                    "id" to "jet-bottom-mask"
                ) {
                    "rect"(
                        "fill" to "#fff",
                        "x" to -100,
                        "y" to -100,
                        "width" to 200,
                        "height" to 200
                    )

                    if (config.tiltX < 0)
                        "ellipse"(
                            "rx" to baseRadius,
                            "ry" to baseRadius * abs(zFactor),
                            "cx" to 0,
                            "cy" to -ellipseY,
                            "fill" to "#000"
                        )
                }
            }

            "path"(
                "id" to "jet-bottom",
//                "mask" to "url(#jet-bottom-mask)",
                "d" to path {
                    move(jetEnd)
                    line(curveEnd)
                    quad(controlPoint, curveStart)
                    line(curveStart.flipX())
                    quad(controlPoint.flipX(), curveEnd.flipX())
                    line(jetEnd.flipX())
                    closePath()
                }
            ) {
                attributes.putAll(config.style)
            }
            "path"(
                "id" to "jet-bottom-back",
//                "mask" to "url(#jet-bottom-mask)",
                "d" to path {
                    move(jetEnd)
                    line(curveEndBack)
                    quad(controlPointBack, curveStartBack)
                    line(curveStartBack.flipX())
                    quad(controlPointBack.flipX(), curveEndBack.flipX())
                    line(jetEnd.flipX())
                    closePath()
                }
            ) {
                attributes.putAll(config.style)
            }
//            "path" {
//                attributes(
//                    "id" to "jet-bottom-mask",
//                    "d" to path {
//                        move(curveStartBack)
//                        arc(baseRadius, baseRadius * abs(zFactor), 0, config.tiltX > 0, config.tiltX < 0, curveStartBack.flipX())
//                        quad(controlPointBack.flipX(), curveEndBack.flipX())
//                        line(jetEnd.flipX())
//                        closePath()
//                    }
//                )
//                if(config.tiltX > 0)
//                    attributes.putAll(config.style)
//                else
//                    attributes.putAll(eventHorizonStyle)
//            }

        }
    }

    private fun generateTopJetBase(): Pair<Double, Node> {
        val circleX = findContactX(config.baseSize, avatar.root.tiltRatio)
        val rx = avatar.eventHorizonRadius * config.baseSize
        val ry = rx * avatar.root.tiltRatio
        val ellipseX = circleX / config.baseSize
        val circleY = sqrt(1 - circleX * circleX)
        val ellipseY = sqrt(1 - ellipseX * ellipseX)

        val circleContactY = circleY * avatar.eventHorizonRadius
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