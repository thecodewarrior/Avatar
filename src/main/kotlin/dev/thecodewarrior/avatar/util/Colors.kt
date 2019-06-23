package dev.thecodewarrior.avatar.util

import java.awt.Color
import kotlin.math.abs

fun distinctColor(seed: Any): Color {
    return distinctColors[abs(seed.hashCode()) % distinctColors.size]
}
val distinctColors = listOf(
    Color(0xe6194b),
    Color(0x3cb44b),
    Color(0xffe119),
    Color(0x4363d8),
    Color(0xf58231),
    Color(0x911eb4),
    Color(0x46f0f0),
    Color(0xf032e6),
    Color(0xbcf60c),
    Color(0xfabebe),
    Color(0x008080),
    Color(0xe6beff),
    Color(0x9a6324),
    Color(0xfffac8),
    Color(0x800000),
    Color(0xaaffc3),
    Color(0x808000),
    Color(0xffd8b1),
    Color(0x000075),
    Color(0x808080),
    Color(0xffffff),
    Color(0x000000)
)

fun Color(hex: String): Color = Color.decode(hex)
val Color.hex: String get() = "%02x%02x%02x".format(this.red, this.green, this.blue)
val Color.rgba: String get() = "%02x%02x%02x%02x".format(this.red, this.green, this.blue, this.alpha)
val Color.argb: String get() = "%02x%02x%02x%02x".format(this.alpha, this.red, this.green, this.blue)
