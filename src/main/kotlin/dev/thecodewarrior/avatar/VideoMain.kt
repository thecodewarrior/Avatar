package dev.thecodewarrior.avatar

import io.humble.video.Codec
import io.humble.video.Muxer
import io.humble.video.Rational
import org.apache.batik.transcoder.TranscoderException
import java.io.File
import javax.imageio.ImageIO
import io.humble.video.Codec.findEncodingCodec
import io.humble.video.Codec.findEncodingCodecByName
import io.humble.video.Encoder
import io.humble.video.MuxerFormat
import io.humble.video.PixelFormat
import javax.swing.Spring.height
import io.humble.video.MediaPicture
import io.humble.video.awt.MediaPictureConverter
import com.sun.corba.se.impl.protocol.giopmsgheaders.MessageBase.setFlag
import io.humble.video.Coder
import io.humble.video.ContainerFormat
import com.sun.xml.internal.ws.streaming.XMLStreamReaderUtil.close
import java.lang.reflect.Array.getDouble
import io.humble.video.awt.MediaPictureConverterFactory
import java.awt.image.BufferedImage
import io.humble.video.awt.MediaPictureConverterFactory.convertToType
import io.humble.video.MediaPacket
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

object VideoMain {
    private var exportImage = true

    init {
        System.setProperty("java.awt.headless", "true")
    }

    @JvmStatic
    fun main(args: Array<String>) {

        print("Creating renderer... ")
        val renderer = SvgRenderer()
        println("done.")

        do {
            exportVideo(renderer)
            print("Press return to run again. Type `:q` and press return to exit > ")
        } while(readLine() != ":q")
    }

    fun exportVideo(renderer: SvgRenderer) {
        val svgOut = File("tmp.svg")
        val videoOut = File("out.mov")

        val fps = 20
        val seconds = 20.0
        val frameCount = (seconds * fps).toInt()

        println("Generating %.2f seconds of video at %d frames per second (total of %d frames)".format(
            seconds, fps, frameCount
        ))

        val root = SvgRoot()
        root.imageWidth = 1920
//        root.viewBox = ViewBox.SQUARE_AVATAR
        root.viewBox = ViewBox("tight boi", -80, -80, 160, 90)

        val videoEncoder = VideoEncoder(
            fps, root.imageWidth, root.imageHeight, videoOut, null, null
        )

        var finished = false
        try {
            repeat(frameCount) { frame ->
                print("Frame %02d: ".format(frame))
                print("Configuring SVG... ")
                configure(root, frame/fps.toDouble())
                print("done. ")

                print("Generating SVG... ")
                svgOut.writeText(root.generate().toString(true))
                print("done. ")

                print("Rendering image... ")
                val image = renderer.render(svgOut)
                print("done. ")

                print("Encoding frame... ")
                videoEncoder.pushFrame(image, frame)
                print("done. ")

                println("Frame %02d complete.".format(frame))
            }

            print("Finishing video... ")
            videoEncoder.finish()
            finished = true
            println("done. ")
        } catch(e: TranscoderException) {
            println("error:")
            e.printStackTrace()
        } finally {
            if(!finished)
                videoEncoder.muxer.close()
        }

    }

    /**
     * Put in a separate method so it can be hotswapped. Hotswapping won't change the already running method.
     */
    fun configure(root: SvgRoot, seconds: Double) {
        val timeAngle = seconds * PI
        root.avatar.apply {
            tiltX = Math.toRadians(-13.0)
            halo = false
            accretionDisk = true

            jets = true
            jetConfig.apply {
                tiltZ = Math.toRadians(-45 + 20 * sin(timeAngle))
                tiltX = Math.toRadians(-10 + 20 * cos(timeAngle))
                baseSize = 0.6
                exitAngle = 0.2
                startDistance = 50.0

                debugStyle = mapOf(
                    "stroke" to "#f00",
                    "stroke-width" to 0.25,
                    "fill" to "none"
                )
                style = mapOf(
//                "stroke" to "#0f0",
//                "stroke-width" to 0.25,
                    "fill" to "none"
                )
            }
        }
    }

    class VideoEncoder(
        fps: Int,
        width: Int,
        height: Int,
        file: File,
        formatName: String?,
        codecName: String?
    ) {

        val framerate: Rational
        val muxer: Muxer
        val encoder: Encoder
        var converter: MediaPictureConverter? = null
        val picture: MediaPicture
        val packet: MediaPacket
        val imageBuffer = BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR)

        // heavily sourced from https://github.com/artclarke/humble-video/blob/master/humble-video-demos/src/main/java/io/humble/video/demos/RecordAndEncodeVideo.java
        init {
            // First we create a muxer using the passed in filename and formatname if given.

            framerate = Rational.make(1, fps)
            muxer = Muxer.make(file.absolutePath, null, formatName)

            /** Now, we need to decide what type of codec to use to encode video. Muxers
             * have limited sets of codecs they can use. We're going to pick the first one that
             * works, or if the user supplied a codec name, we're going to force-fit that
             * in instead.
             */
            val format = muxer.format
            val codec: Codec = if (codecName != null) {
                Codec.findEncodingCodecByName(codecName)
            } else {
                Codec.findEncodingCodec(format.defaultVideoCodecId)
            }

            /**
             * Now that we know what codec, we need to create an encoder
             */
            encoder = Encoder.make(codec)

            /**
             * Video encoders need to know at a minimum:
             *   width
             *   height
             *   pixel format
             * Some also need to know frame-rate (older codecs that had a fixed rate at which video files could
             * be written needed this). There are many other options you can set on an encoder, but we're
             * going to keep it simpler here.
             */
            encoder.width = width
            encoder.height = height
            // We are going to use 420P as the format because that's what most video formats these days use
            val pixelformat = PixelFormat.Type.PIX_FMT_YUV420P
            encoder.pixelFormat = pixelformat
            encoder.timeBase = framerate

            /** An annoynace of some formats is that they need global (rather than per-stream) headers,
             * and in that case you have to tell the encoder. And since Encoders are decoupled from
             * Muxers, there is no easy way to know this beyond
             */
            if (format.getFlag(ContainerFormat.Flag.GLOBAL_HEADER))
                encoder.setFlag(Coder.Flag.FLAG_GLOBAL_HEADER, true)

            /** Open the encoder. */
            encoder.open(null, null)

            /** Add this stream to the muxer. */
            muxer.addNewStream(encoder)

            /** And open the muxer for business. */
            muxer.open(null, null)

            /** Next, we need to make sure we have the right MediaPicture format objects
             * to encode data with. Java (and most on-screen graphics programs) use some
             * variant of Red-Green-Blue image encoding (a.k.a. RGB or BGR). Most video
             * codecs use some variant of YCrCb formatting. So we're going to have to
             * convert. To do that, we'll introduce a MediaPictureConverter object later. object.
             */
            picture = MediaPicture.make(
                encoder.width,
                encoder.height,
                pixelformat)
            picture.timeBase = framerate

            packet = MediaPacket.make()
        }

        fun pushFrame(image: BufferedImage, frame: Int) {

            val g = imageBuffer.createGraphics()
            g.clearRect(0, 0, imageBuffer.width, imageBuffer.height)
            g.drawImage(image, 0, 0, null)
            g.finalize()

            /** This is LIKELY not in YUV420P format, so we're going to convert it using some handy utilities.  */
            val converter = this.converter ?: MediaPictureConverterFactory.createConverter(imageBuffer, picture)
            this.converter = converter

            converter.toPicture(picture, imageBuffer, frame.toLong())

            do {
                encoder.encode(packet, picture)
                if (packet.isComplete)
                    muxer.write(packet, false)
            } while (packet.isComplete)
        }

        fun finish() {
            /** Encoders, like decoders, sometimes cache pictures so it can do the right key-frame optimizations.
             * So, they need to be flushed as well. As with the decoders, the convention is to pass in a null
             * input until the output is not complete.
             */
            do {
                encoder.encode(packet, null)
                if (packet.isComplete)
                    muxer.write(packet, false)
            } while (packet.isComplete)

            /** Finally, let's clean up after ourselves. */
            muxer.close()
        }
    }
}