package dev.thecodewarrior.avatar

import org.apache.batik.transcoder.TranscoderException
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
        exportImage = false
        root.imageWidth = 2000
//        root.viewBox = ViewBox("tight boi", -31, -11, 4, 4)
//        root.viewBox = ViewBox("tight boi", -35, -45, 70, 90)
//        root.viewBox = ViewBox("tight boi", -80, 0, 80, 80)
        root.viewBox = ViewBox.SQUARE_AVATAR
//        root.avatar.tilt = 0.0
        root.tiltRatio = 1/90.0

        root.avatar.apply {
            tiltX = Math.toRadians(-30.0)
            eventHorizonStyle = mapOf(
                "stroke" to "#0f0",
                "stroke-width" to 0.1,
                "fill" to "none"
            )

            halo = false
            accretionDisk = true
            accretionDiskStyle = mapOf(
                "stroke" to "#0f0",
                "stroke-width" to 0.1,
                "fill" to "none"
            )

            jets = true
            jetBaseSize = 0.5
            jetExitAngle = 0.5
            jetStartDistance = 20.0
            jetStyle = mapOf(
                "stroke" to "#f00",
                "stroke-width" to 0.1,
                "fill" to "none"
            )
        }
//        root.avatar.jetExitAngle = 0.75

        return root
    }
}