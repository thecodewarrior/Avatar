package dev.thecodewarrior.avatar

import dev.thecodewarrior.avatar.generator.SvgGenerator
import org.apache.batik.transcoder.TranscoderException
import org.redundent.kotlin.xml.PrintOptions
import org.redundent.kotlin.xml.xml
import java.io.File
import javax.imageio.ImageIO

object Main {
    private var exportImage = true

    init {
        System.setProperty("java.awt.headless", "true")
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val svgOut = File("out.svg")
        val pngOut = File("out.png")

        print("Creating renderer... ")
        val renderer = SvgRenderer()
        println("done.")

        do {
            print("Configuring... ")
            val root = getRoot()
            println("done.")

            print("Writing... ")
            svgOut.writeText(root.generate().toString(true))
            try {
                if(exportImage)
                    ImageIO.write(renderer.render(svgOut), "png", pngOut)
                println("done.")
            } catch(e: TranscoderException) {
                println("error:")
                e.printStackTrace()
            }

            print("Press return to run again. Type `:q` and press return to exit > ")
        } while(readLine() != ":q")
    }

    /**
     * Put in a separate method so it can be hotswapped. Hotswapping won't change the already running method.
     */
    fun getRoot(): SvgRoot {
        val root = SvgRoot()
        exportImage = true
        root.imageWidth = 500
//        root.viewBox = ViewBox("tight boi", -31, -11, 4, 4)
//        root.viewBox = ViewBox("tight boi", -35, -45, 70, 90)
//        root.viewBox = ViewBox("tight boi", -30, -30, 60, 15)
        root.viewBox = ViewBox.SQUARE_AVATAR
//        root.avatar.tilt = 0.0
        root.tiltRatio = 3/9.0
        root.avatar.halo = false
        root.avatar.jets = true
        root.avatar.accretionDisk = true
        root.avatar.jetBaseSize = 0.5
//        root.avatar.jetExitAngle = 0.75

        return root
    }
}