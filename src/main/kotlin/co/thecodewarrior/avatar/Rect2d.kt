package co.thecodewarrior.avatar

import kotlin.math.max
import kotlin.math.min

class Rect2d(min: Vec2d, max: Vec2d) {
    val min = vec(min(min.x, max.x), min(min.y, max.y))
    val max = vec(max(min.x, max.x), max(min.y, max.y))
    val width: Double
        get() = max.x-min.x
    val height: Double
        get() = max.y-min.y
}
