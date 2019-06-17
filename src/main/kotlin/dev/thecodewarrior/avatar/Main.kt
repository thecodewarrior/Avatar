package dev.thecodewarrior.avatar

import dev.thecodewarrior.avatar.generator.SvgGenerator
import java.io.File
import javax.imageio.ImageIO

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val svgOut = File("out.svg")
        val pngOut = File("out.png")

        val renderer = SvgRenderer()
        val generator = SvgGenerator()
        val svgText = generator.generate()
        svgOut.writeText(svgText)
        ImageIO.write(renderer.render(svgOut), "png", pngOut)
    }
}