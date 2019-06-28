package dev.thecodewarrior.avatar

import dev.thecodewarrior.avatar.dsl.CssTextElement
import dev.thecodewarrior.avatar.dsl.Path
import dev.thecodewarrior.avatar.dsl.addElement
import dev.thecodewarrior.avatar.generator.*
import dev.thecodewarrior.avatar.generator.avatar.AvatarObject
import org.redundent.kotlin.xml.Node
import org.redundent.kotlin.xml.xml
import kotlin.math.roundToInt

class SvgRoot: SvgObject() {
    var viewBox: ViewBox = ViewBox.FULL
    var imageWidth: Int = 1000
    val imageHeight: Int
    get() {
        val aspectRatio = viewBox.height.toDouble()/viewBox.width
        return (aspectRatio * imageWidth).roundToInt()
    }
    var generateGuides: Boolean = false
    var tiltRatio = 2/9.0

    val avatar = AvatarObject(this)

    val pixelsPerPoint: Int
        get() = imageWidth/viewBox.width
    val pointsPerPixel: Double
        get() = viewBox.width.toDouble()/imageWidth

    // y-flipping code is from: https://stackoverflow.com/a/38811507/1541907
    override fun generate(): Node = xml("svg") {
        xmlns = "http://www.w3.org/2000/svg"

        attributes(
            "xmlns:xlink" to "http://www.w3.org/1999/xlink",
            "x" to "0px",
            "y" to "0px",
            "width" to "${imageWidth}px",
            "height" to "${imageHeight}px",
            "viewBox" to "${viewBox.minX} ${viewBox.minY} ${viewBox.width} ${viewBox.height}",
            "xml:space" to "preserve",
            "shape-rendering" to "geometricPrecision",

            // flip
            "class" to "cartesian",
            "preserveAspectRatio" to "xMidYMid meet"
        )

        "style"().apply {
            addElement(CssTextElement("""
                svg.cartesian > g text {
                    transform: scaleY(-1);
                }
            """.trimIndent()))
        }

        "rect" {
            attributes(
                "id" to "background",
                "x" to viewBox.minX,
                "y" to viewBox.minY,
                "width" to viewBox.width,
                "height" to viewBox.height,
                "fill" to "#000"
            )
        }

        Path.arcYFlipped = true
        "g" {
            attributes(
                "transform" to "scale(1 -1)"
            )

            addNode(avatar.generate())

            if (generateGuides) {
//            ViewBox.values().forEach {
//                addNode(it.generateGuide())
//            }
            }
        }
        Path.arcYFlipped = false
    }
}
