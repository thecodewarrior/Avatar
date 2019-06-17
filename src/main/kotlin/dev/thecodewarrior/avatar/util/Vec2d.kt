package dev.thecodewarrior.avatar.util

import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class Vec2d(val x: Double, val y: Double) {
    val xf: Float get() = x.toFloat()
    val yf: Float get() = y.toFloat()
    val xi: Int get() = x.toInt()
    val yi: Int get() = y.toInt()

    fun floor(): Vec2d {
        return Vec2d(Math.floor(x), Math.floor(y))
    }

    fun ceil(): Vec2d {
        return Vec2d(Math.ceil(x), Math.ceil(y))
    }

    fun round(): Vec2d {
        return Vec2d(Math.round(x).toDouble(), Math.round(y).toDouble())
    }

    fun setX(value: Double): Vec2d {
        return Vec2d(value, y)
    }

    fun setY(value: Double): Vec2d {
        return Vec2d(x, value)
    }

    @JvmSynthetic
    operator fun plus(other: Vec2d): Vec2d = add(other)
    fun add(other: Vec2d): Vec2d {
        return Vec2d(x + other.x, y + other.y)
    }

    fun add(otherX: Double, otherY: Double): Vec2d {
        return Vec2d(x + otherX, y + otherY)
    }

    @JvmSynthetic
    operator fun minus(other: Vec2d): Vec2d = sub(other)
    fun sub(other: Vec2d): Vec2d {
        return Vec2d(x - other.x, y - other.y)
    }

    fun sub(otherX: Double, otherY: Double): Vec2d {
        return Vec2d(x - otherX, y - otherY)
    }

    @JvmSynthetic
    operator fun times(other: Vec2d): Vec2d = mul(other)
    fun mul(other: Vec2d): Vec2d {
        return Vec2d(x * other.x, y * other.y)
    }

    fun mul(otherX: Double, otherY: Double): Vec2d {
        return Vec2d(x * otherX, y * otherY)
    }

    @Suppress("NOTHING_TO_INLINE")
    inline operator fun times(amount: Number): Vec2d = mul(amount.toDouble())
    fun mul(amount: Double): Vec2d {
        return Vec2d(x * amount, y * amount)
    }

    @JvmSynthetic
    operator fun div(other: Vec2d): Vec2d = divide(other)
    fun divide(other: Vec2d): Vec2d {
        return Vec2d(x / other.x, y / other.y)
    }

    fun divide(otherX: Double, otherY: Double): Vec2d {
        return Vec2d(x / otherX, y / otherY)
    }

    @Suppress("NOTHING_TO_INLINE")
    inline operator fun div(amount: Number): Vec2d = divide(amount.toDouble())
    fun divide(amount: Double): Vec2d {
        return Vec2d(x / amount, y / amount)
    }

    infix fun dot(point: Vec2d): Double {
        return x * point.x + y * point.y
    }

    @JvmSynthetic
    operator fun unaryMinus(): Vec2d = this * -1

    fun flipX(): Vec2d = Vec2d(-x, y)
    fun flipY(): Vec2d = Vec2d(x, -y)
    fun flipXY(): Vec2d = Vec2d(-x, -y)

    @get:JvmName("lengthSquared")
    val lengthSquared: Double get() = x * x + y * y

    fun length(): Double {
        return sqrt(x * x + y * y)
    }

    fun normalize(): Vec2d {
        val norm = length()
        return Vec2d(x / norm, y / norm)
    }

    fun squareDist(vec: Vec2d): Double {
        val d0 = vec.x - x
        val d1 = vec.y - y
        return d0 * d0 + d1 * d1
    }

    fun projectOnTo(other: Vec2d): Vec2d {
        val norm = other.normalize()
        return norm.mul(this.dot(norm))
    }

    fun rotate(theta: Float): Vec2d {
        if (theta == 0f) return this

        val cs = cos(theta)
        val sn = sin(theta)
        return Vec2d(
            this.x * cs - this.y * sn,
            this.x * sn + this.y * cs
        )
    }

    //=============================================================================

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        var temp: Long
        temp = java.lang.Double.doubleToLongBits(x)
        result = prime * result + (temp xor temp.ushr(32)).toInt()
        temp = java.lang.Double.doubleToLongBits(y)
        result = prime * result + (temp xor temp.ushr(32)).toInt()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (other == null)
            return false
        if (javaClass != other.javaClass)
            return false
        return x == (other as Vec2d).x && y == other.y
    }

    override fun toString(): String {
        return "($x,$y)"
    }
}
