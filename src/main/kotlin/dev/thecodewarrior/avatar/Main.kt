package dev.thecodewarrior.avatar

import dev.thecodewarrior.avatar.generator.SvgGenerator
import java.io.ByteArrayInputStream
import java.io.File
import java.io.StringReader
import java.net.URL
import java.io.IOException
import org.apache.batik.transcoder.TranscoderException
import org.apache.batik.transcoder.TranscoderOutput
import java.awt.image.BufferedImage
import java.io.FileInputStream
import org.apache.batik.transcoder.TranscoderInput
import org.apache.batik.transcoder.SVGAbstractTranscoder.KEY_USER_STYLESHEET_URI
import org.apache.batik.transcoder.XMLAbstractTranscoder.KEY_DOCUMENT_ELEMENT
import org.apache.batik.util.SVGConstants
import org.apache.batik.transcoder.XMLAbstractTranscoder.KEY_DOCUMENT_ELEMENT_NAMESPACE_URI
import org.apache.batik.anim.dom.SVGDOMImplementation
import org.apache.batik.transcoder.XMLAbstractTranscoder.KEY_DOM_IMPLEMENTATION
import org.apache.batik.transcoder.XMLAbstractTranscoder.KEY_XML_PARSER_VALIDATING
import org.apache.batik.transcoder.TranscodingHints
import org.apache.batik.transcoder.image.ImageTranscoder
import org.apache.commons.io.FileUtils
import javax.imageio.ImageIO

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val svgOut = File("out.svg")
        val pngOut = File("out.png")

        val generator = SvgGenerator()
        val svgText = generator.generate()
        svgOut.writeText(svgText)

        val css = """
            svg {
                shape-rendering: geometricPrecision;
                text-rendering:  geometricPrecision;
                color-rendering: optimizeQuality;
                image-rendering: optimizeQuality;
            }
        """.trimIndent()

        val cssFile = File.createTempFile("batik-default-override-", ".css")
        FileUtils.writeStringToFile(cssFile, css)

        val transcoderHints = TranscodingHints()
        transcoderHints[ImageTranscoder.KEY_XML_PARSER_VALIDATING] = java.lang.Boolean.FALSE
        transcoderHints[ImageTranscoder.KEY_DOM_IMPLEMENTATION] = SVGDOMImplementation.getDOMImplementation()
        transcoderHints[ImageTranscoder.KEY_DOCUMENT_ELEMENT_NAMESPACE_URI] = SVGConstants.SVG_NAMESPACE_URI
        transcoderHints[ImageTranscoder.KEY_DOCUMENT_ELEMENT] = "svg"
        transcoderHints[ImageTranscoder.KEY_USER_STYLESHEET_URI] = cssFile.toURI().toString()

        val imagePointer = Array<BufferedImage?>(1) { null }
        try {

            val input = TranscoderInput(FileInputStream(svgOut))

            val t = object: ImageTranscoder() {

                override fun createImage(w: Int, h: Int): BufferedImage {
                    return BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB)
                }

                @Throws(TranscoderException::class)
                override fun writeImage(image: BufferedImage, out: TranscoderOutput?) {
                    imagePointer[0] = image
                }
            }
            t.transcodingHints = transcoderHints
            t.transcode(input, null)
        } catch (ex: TranscoderException) {
            // Requires Java 6
            ex.printStackTrace()
            throw IOException("Couldn't convert $svgOut")
        } finally {
            cssFile.delete()
        }

        val image = imagePointer[0]!!
        ImageIO.write(image, "png", pngOut)
    }
}