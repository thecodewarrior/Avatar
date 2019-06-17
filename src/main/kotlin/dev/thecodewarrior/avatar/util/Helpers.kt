package dev.thecodewarrior.avatar.util

import dev.thecodewarrior.avatar.dsl.Path

@Suppress("NOTHING_TO_INLINE")
inline fun vec(x: Number, y: Number) = Vec2d(x.toDouble(), y.toDouble())

inline fun path(callback: Path.() -> Unit): String = Path.build(callback)

/**
 * Based on:
 * https://en.wikipedia.org/wiki/Intersection_(Euclidean_geometry)#Two_lines
 *
 * @param p1 The first point
 * @param d1 The first direction
 * @param p2 The second point
 * @param d2 The second direction
 * @return The point where the two lines intersect, or null if they are parallel.
 */
fun intersectLines(p1: Vec2d, d1: Vec2d, p2: Vec2d, d2: Vec2d): Vec2d? {
    val l1 = LineEquation(p1, d1)
    val l2 = LineEquation(p2, d2)

    if((l1.a * l2.b - l2.a * l1.b) == 0.0) // lines are parallel
        return null

    return vec(
        (l1.c * l2.b - l2.c * l1.b) / (l1.a * l2.b - l2.a * l1.b),
        (l1.a * l2.c - l2.a * l1.c) / (l1.a * l2.b - l2.a * l1.b)
    )
}

/**
 * Originally based on this, however the terms have been simplified considerably:
 * https://bobobobo.wordpress.com/2008/01/07/solving-linear-equations-ax-by-c-0/
 *
 * Line in the standard form: `ax + by = c`
 */
private class LineEquation(val point: Vec2d, val direction: Vec2d) {
    val a = -direction.y
    val b = direction.x
    val c = direction.x*point.y - point.x*direction.y
}
