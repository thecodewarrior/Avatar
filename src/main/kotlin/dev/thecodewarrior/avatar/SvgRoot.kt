package dev.thecodewarrior.avatar

import dev.thecodewarrior.avatar.generator.*
import org.redundent.kotlin.xml.Node
import org.redundent.kotlin.xml.xml
import kotlin.math.roundToInt

class SvgRoot: SvgGenerator {
    var viewBox: ViewBox = ViewBox.FULL
    var imageWidth: Int = 1000
    var generateGuides: Boolean = false
    var tiltRatio = 2/9.0

    val avatar = AvatarGenerator(this)

    override val pixelsPerPoint: Int
        get() = imageWidth/viewBox.width

    override fun generate(): Node = xml("svg") {
        xmlns = "http://www.w3.org/2000/svg"

        val aspectRatio = viewBox.height.toDouble()/viewBox.width
        attributes(
            "xmlns:xlink" to "http://www.w3.org/1999/xlink",
            "x" to "0px",
            "y" to "0px",
            "width" to "${imageWidth}px",
            "height" to "${(aspectRatio * imageWidth).roundToInt()}px",
            "viewBox" to "${viewBox.minX.p} ${viewBox.minY.p} ${viewBox.width.p} ${viewBox.height.p}",
            "xml:space" to "preserve"
        )

        "rect" {
            attributes(
                "id" to "background",
                "x" to viewBox.minX.p,
                "y" to viewBox.minY.p,
                "width" to viewBox.width.p,
                "height" to viewBox.height.p,
                "fill" to "#000"
            )
        }

        addNode(avatar.generate())

        if(generateGuides) {
//            ViewBox.values().forEach {
//                addNode(it.generateGuide())
//            }
        }
    }

}