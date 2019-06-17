package dev.thecodewarrior.avatar.generator

import dev.thecodewarrior.avatar.SvgRoot

interface SvgGeneratorBase: SvgGenerator {
    val root: SvgGenerator
    override val pixelsPerPoint: Int
        get() = root.pixelsPerPoint
}