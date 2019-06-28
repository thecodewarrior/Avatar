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
        root.imageWidth = 1000
//        root.viewBox = ViewBox("tight boi", -31, -11, 4, 4)
//        root.viewBox = ViewBox("tight boi", -35, -45, 70, 90)
//        root.viewBox = ViewBox("tight boi", -40, -40, 80, 40)
        root.viewBox = ViewBox.SQUARE_AVATAR
//        root.viewBox = ViewBox("tight boi", -80, -80, 100, 100)

        root.avatar.apply {
            tiltX = Math.toRadians(-13.0)
            halo = false
            accretionDisk = true

            jets = true
            jetConfig.apply {
//                tiltX = Math.toRadians(-15.0)
                baseSize = 0.6
                exitAngle = 0.2
//                startDistance = 20.0

//                debugColors = true
//                style = mapOf(
//                "stroke" to "#0f0",
//                "stroke-width" to 0.01,
//                    "fill" to "#fff"
//                )
            }

            eventHorizonStyle = mapOf(
//                "stroke" to "#0f0",
//                "stroke-width" to 0.01,
                "fill" to "#000"
            )
        }

        return root
    }
}