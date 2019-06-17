package dev.thecodewarrior.avatar

import org.redundent.kotlin.xml.Node
import org.redundent.kotlin.xml.xml

class ViewBox(val name: String, val minX: Int, val minY: Int, val width: Int, val height: Int, val ellipse: Boolean = false) {
    constructor(name: String, rx: Int, ry: Int, ellipse: Boolean = false): this(name, -rx, -ry, rx*2, ry*2, ellipse)

    fun generateGuide(): Node {
        val node: Node
        if(this.ellipse) {
            node = xml("ellipse") {
                attributes(
                    "cx" to (minX+width)/2,
                    "cy" to (minY+height)/2,
                    "rx" to width/2,
                    "ry" to height/2
                )
            }
        } else {
            node = xml("rect") {
                attributes(
                    "x" to minX,
                    "y" to minY,
                    "width" to width,
                    "height" to height
                )
            }
        }
        node.apply {
            attributes(
                "id" to this@ViewBox.name,
                "stroke" to "#F00",
                "stroke-width" to 1,
                "fill" to "none"
            )
        }
        return node
    }

    companion object {
        val FULL = ViewBox("full", 500, 500)
        val ROUND_AVATAR = ViewBox("round avatar", 100, 100, ellipse = true)
        val SQUARE_AVATAR = ViewBox("square avatar", 80, 80)
        val TWITTER_BANNER = ViewBox("twitter banner", 450, 150)
    }
}
