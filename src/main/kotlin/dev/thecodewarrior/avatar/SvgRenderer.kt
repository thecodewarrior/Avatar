package dev.thecodewarrior.avatar

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
import java.io.ByteArrayInputStream
import java.io.Closeable
import java.io.File
import java.io.InputStream
import javax.imageio.ImageIO

class SvgRenderer: Closeable {
    private lateinit var output: BufferedImage
    private val cssFile = File.createTempFile("batik-default-override-", ".css")
    var css: String = ""
        set(value) {
            field = value
            cssFile.writeText(value)
        }

    val transcoder = object: ImageTranscoder() {
        override fun createImage(w: Int, h: Int): BufferedImage {
            return BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB)
        }

        @Throws(TranscoderException::class)
        override fun writeImage(image: BufferedImage, out: TranscoderOutput?) {
            output = image
        }
    }

    init {
        this.css = """
            svg {
                shape-rendering: geometricPrecision;
                text-rendering:  geometricPrecision;
                color-rendering: optimizeQuality;
                image-rendering: optimizeQuality;
            }
        """.trimIndent()

        val transcoderHints = TranscodingHints()
        transcoderHints[ImageTranscoder.KEY_XML_PARSER_VALIDATING] = java.lang.Boolean.FALSE
        transcoderHints[ImageTranscoder.KEY_DOM_IMPLEMENTATION] = SVGDOMImplementation.getDOMImplementation()
        transcoderHints[ImageTranscoder.KEY_DOCUMENT_ELEMENT_NAMESPACE_URI] = SVGConstants.SVG_NAMESPACE_URI
        transcoderHints[ImageTranscoder.KEY_DOCUMENT_ELEMENT] = "svg"
        transcoderHints[ImageTranscoder.KEY_USER_STYLESHEET_URI] = cssFile.toURI().toString()

        transcoder.transcodingHints = transcoderHints
    }

    override fun close() {
        cssFile.delete()
    }

    @Throws(TranscoderException::class)
    fun render(svg: String): BufferedImage = render(ByteArrayInputStream(svg.toByteArray()))

    @Throws(TranscoderException::class)
    fun render(svg: File): BufferedImage = render(FileInputStream(svg))

    @Throws(TranscoderException::class)
    fun render(svg: InputStream): BufferedImage {
        transcoder.transcode(TranscoderInput(svg), null)
        return output
    }
}