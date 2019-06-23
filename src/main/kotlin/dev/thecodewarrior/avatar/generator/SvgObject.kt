package dev.thecodewarrior.avatar.generator

import dev.thecodewarrior.avatar.util.Vec2d
import org.redundent.kotlin.xml.Node
import org.redundent.kotlin.xml.xml

abstract class SvgObject {
    abstract fun generate(): Node
}