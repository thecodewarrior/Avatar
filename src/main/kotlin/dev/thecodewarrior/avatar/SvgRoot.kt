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
    var generateGuides: Boolean = false
    var tiltRatio = 2/9.0

    val avatar = AvatarObject(this)

    val pixelsPerPoint: Int
        get() = imageWidth/viewBox.width

    // y-flipping code is from: https://stackoverflow.com/a/38811507/1541907
    override fun generate(): Node = xml("svg") {
        xmlns = "http://www.w3.org/2000/svg"

        val aspectRatio = viewBox.height.toDouble()/viewBox.width
        attributes(
            "xmlns:xlink" to "http://www.w3.org/1999/xlink",
            "x" to "0px",
            "y" to "0px",
            "width" to "${imageWidth}px",
            "height" to "${(aspectRatio * imageWidth).roundToInt()}px",
            "viewBox" to "${viewBox.minX} ${viewBox.minY} ${viewBox.width} ${viewBox.height}",
            "xml:space" to "preserve",
            "shape-rendering" to "geometricPrecision",

            // flip
            "class" to "cartesian",
            "preserveAspectRatio" to "xMidYMid meet"
        )

        "style"().apply {
            addElement(CssTextElement("""
                svg.cartesian {
                    display:flex;
                }
                /* Flip the vertical axis in `g` to emulate cartesian. */
                svg.cartesian > g {
                    transform: scaleY(-1);
                }
                /* Re-flip all `text` element descendants to their original side up. */
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
