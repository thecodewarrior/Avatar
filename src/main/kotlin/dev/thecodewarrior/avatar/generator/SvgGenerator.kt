package dev.thecodewarrior.avatar.generator

import dev.thecodewarrior.avatar.util.Vec2d
import org.redundent.kotlin.xml.Node
import org.redundent.kotlin.xml.xml

interface SvgGenerator {
    val pixelsPerPoint: Int
    fun generate(): Node

    /**
     * Converts points to pixels. Used to get around svg decimal limitations.
     *
     * DO NOT STORE THIS. Calculate it at the last possible moment, because doing so before it's necessary only causes
     * confusion.
     */
    val Number.p: Double get() = this.toDouble() * pixelsPerPoint

    /**
     * Converts points to pixels. Used to get around svg decimal limitations.
     *
     * DO NOT STORE THIS. Calculate it at the last possible moment, because doing so before it's necessary only causes
     * confusion.
     */
    val Vec2d.p: Vec2d get() = Vec2d(x * pixelsPerPoint, y * pixelsPerPoint)
}