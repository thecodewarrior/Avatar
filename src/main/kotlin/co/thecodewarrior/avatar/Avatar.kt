package co.thecodewarrior.avatar

import kotlin.math.*

class Avatar(settings: AvatarSettings): AvatarXML(settings) {

    val blackHoleRadius = 30
    val haloSize = 10
    val width = 100

    override fun gen() {
        println(""" <g id="avatar" transform="rotate(-45)"> """)
        if(settings.enableHalo) {
            println(""" <circle id="halo" cx="0" cy="0" r="${blackHoleRadius + haloSize}" fill="#FFF"/> """)
        }

        println("""<circle id="event-horizon-back" cx="0" cy="0" r="$blackHoleRadius" fill="#000"/>""")

        println("""<ellipse id="accretion-disk" cx="0" cy="0" rx="$width" ry="${width*settings.heightRatio}" fill="#FFF"/>""")

        println("""<path id="event-horizon-front" d="
            M -$blackHoleRadius,0
            A 1,1 0 0,1 $blackHoleRadius,0
            A 1,${settings.heightRatio} 0 0,1 -$blackHoleRadius,0
        " fill="#000"/>""")

        if(settings.enableJets) {
            println(AvatarJets(settings, blackHoleRadius).toString())
        }

        println("</g>")
    }
}

class AvatarJets(settings: AvatarSettings, val blackHoleRadius: Int): AvatarXML(settings) {
    var innerAngle = PI/6
    var jetRadius = 5
    var jetSlope = 1/1500.0
    var jetLength = 1500
    var curveEndExtension = 20

    override fun gen() {
        val tangentSlope = tan(innerAngle - PI/2)

        val contactPoint = vec(blackHoleRadius*sin(innerAngle), blackHoleRadius*cos(innerAngle))

        // slopeless jet intersection point
        // y = m(x-x1)+y1
        var controlPoint = vec(jetRadius, tangentSlope*(jetRadius-contactPoint.x)+contactPoint.y)

        val controlPointDistance = (controlPoint - contactPoint).length()
        val curveEndPoint = vec(jetRadius, controlPoint.y + controlPointDistance) +
                vec(curveEndExtension*jetSlope, curveEndExtension)

        if(jetSlope != 0.0) {
            val invSlope = 1/jetSlope
            // sloped jet intersection x
            // x = ( (y2-m2*x2)-(y1-m1*x1) ) / (m1-m2)
            val intersectionX =
                    ( (curveEndPoint.y-invSlope*curveEndPoint.x) - (contactPoint.y-tangentSlope*contactPoint.x) ) /
                            (tangentSlope - invSlope)
            // y = m(x-x1)+y1
            val intersectionY = tangentSlope*(intersectionX-contactPoint.x) + contactPoint.y
            controlPoint = vec(intersectionX, intersectionY)
        }

        val jetEndPoint = vec(jetLength*jetSlope, jetLength)
        val top = jetPath(
                contactPoint = contactPoint,
                controlPoint = controlPoint,
                curveEndPoint = curveEndPoint,
                jetEndPoint = jetEndPoint
        )

        val bottom = jetPath(
                contactPoint = contactPoint.flipX,
                controlPoint = controlPoint.flipX,
                curveEndPoint = curveEndPoint.flipX,
                jetEndPoint = jetEndPoint.flipX
        )

        println("""<g id="astrophysical-jets">""")

        println("<path d=\"$top\" fill=\"#fff\"/>")
        println("<path d=\"$bottom\" fill=\"#fff\"/>")

        println("</g>")
    }

    fun jetPath(contactPoint: Vec2d, controlPoint: Vec2d, curveEndPoint: Vec2d, jetEndPoint: Vec2d): String {
        val path = ArrayList<Any>()

        path.add("M", contactPoint.flipY)
        path.add("A", 1, settings.heightRatio, 0, 0, 0 , contactPoint)

        path.add("Q", controlPoint, curveEndPoint)
        path.add("L", jetEndPoint)

        path.add("L", jetEndPoint.flipY)

        path.add("L", curveEndPoint.flipY)
        path.add("Q", controlPoint.flipY, contactPoint.flipY)

        return path.joinToString(" ") { (it as? Vec2d)?.let { "${it.x},${it.y}" } ?: it.toString() }
    }

    fun <T> MutableList<T>.add(vararg items: T) {
        items.forEach { this.add(it) }
    }
}

