package dev.thecodewarrior.avatar.generator.avatar

import dev.thecodewarrior.avatar.SvgRoot
import dev.thecodewarrior.avatar.dsl.Path
import dev.thecodewarrior.avatar.generator.SvgObject
import dev.thecodewarrior.avatar.util.Vec2d
import dev.thecodewarrior.avatar.util.distinctColor
import dev.thecodewarrior.avatar.util.hex
import dev.thecodewarrior.avatar.util.intersectLines
import dev.thecodewarrior.avatar.util.path
import dev.thecodewarrior.avatar.util.vec
import org.redundent.kotlin.xml.Node
import org.redundent.kotlin.xml.xml
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sign
import kotlin.math.sin
import kotlin.math.sqrt

class AvatarObject(val root: SvgRoot): SvgObject() {
    var eventHorizonStyle: Map<String, Any?> = mapOf("fill" to "#000")
    var haloStyle: Map<String, Any?> = mapOf("fill" to "#fff")
    var accretionDiskStyle: Map<String, Any?> = mapOf("fill" to "#fff")

    var halo = true
    var accretionDisk = true
    var jets = false
    var tiltZ = Math.toRadians(-45.0)
    var tiltX = Math.toRadians(-13.0)

    var eventHorizonRadius = 30
    var haloRadius = 40
    var accretionDiskRadius = 90

    val jetConfig = JetObject.Configuration()

    override fun generate(): Node = xml("g") {
        val zFactor = sin(tiltX)

        attributes(
            "id" to "avatar",
            "transform" to "rotate(${Math.toDegrees(-tiltZ)})"
        )

        if (halo) {
            "circle" {
                attributes(
                    "id" to "halo",
                    "cx" to 0,
                    "cy" to 0,
                    "r" to haloRadius
                )
                attributes.putAll(haloStyle)
            }
        }

        "circle" {
            attributes(
                "id" to "event-horizon-back",
                "cx" to 0,
                "cy" to 0,
                "r" to eventHorizonRadius
            )
            attributes.putAll(eventHorizonStyle)
        }

        val jetObject = JetObject(this@AvatarObject, jetConfig)

        if (jets) {
            addNode(jetObject.generateJets(tiltX >= 0))
        }

        if (accretionDisk) {
            "ellipse" {
                attributes(
                    "id" to "accretion-disk",
                    "cx" to 0,
                    "cy" to 0,
                    "rx" to accretionDiskRadius,
                    "ry" to accretionDiskRadius * abs(zFactor)
                )
                attributes.putAll(accretionDiskStyle)
            }
            "path" {
                attributes(
                    "id" to "event-horizon-front",
                    "d" to path {
                        move(vec(-eventHorizonRadius, 0))
                        arc(1, 1, 0, false, zFactor < 0, vec(eventHorizonRadius, 0))
                        arc(1, zFactor, 0, false, zFactor < 0, vec(-eventHorizonRadius, 0))
                    }
                )
                attributes.putAll(eventHorizonStyle)
            }
        }

        if (jets) {
            addNode(jetObject.generateJets(tiltX < 0))
        }
    }
}