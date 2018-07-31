package co.thecodewarrior.avatar

import kotlin.math.*

class Vec2d(val x: Double, val y: Double) {

    val xf: Float = x.toFloat()
    val yf: Float = y.toFloat()
    val xi: Int = floor(x).toInt()
    val yi: Int = floor(y).toInt()

    fun floor(): Vec2d {
        return Vec2d(floor(x), floor(y))
    }

    fun ceil(): Vec2d {
        return Vec2d(ceil(x), ceil(y))
    }

    fun setX(value: Double): Vec2d {
        return Vec2d(value, y)
    }

    fun setY(value: Double): Vec2d {
        return Vec2d(x, value)
    }

    fun add(other: Vec2d): Vec2d {
        return Vec2d(x + other.x, y + other.y)
    }

    fun add(otherX: Double, otherY: Double): Vec2d {
        return Vec2d(x + otherX, y + otherY)
    }

    fun sub(other: Vec2d): Vec2d {
        return Vec2d(x - other.x, y - other.y)
    }

    fun sub(otherX: Double, otherY: Double): Vec2d {
        return Vec2d(x - otherX, y - otherY)
    }

    fun mul(other: Vec2d): Vec2d {
        return Vec2d(x * other.x, y * other.y)
    }

    fun mul(otherX: Double, otherY: Double): Vec2d {
        return Vec2d(x * otherX, y * otherY)
    }

    fun mul(amount: Double): Vec2d {
        return Vec2d(x * amount, y * amount)
    }

    fun divide(other: Vec2d): Vec2d {
        return Vec2d(x / other.x, y / other.y)
    }

    fun divide(otherX: Double, otherY: Double): Vec2d {
        return Vec2d(x / otherX, y / otherY)
    }

    fun divide(amount: Double): Vec2d {
        return Vec2d(x / amount, y / amount)
    }

    infix fun dot(point: Vec2d): Double {
        return x * point.x + y * point.y
    }

    private val len by lazy { sqrt(x * x + y * y) }

    fun length(): Double {
        return len
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

    fun rotate(theta: Number): Vec2d {
        val cs = cos(theta.toFloat())
        val sn = sin(theta.toFloat())
        return Vec2d(
                this.x * cs - this.y * sn,
                this.x * sn + this.y * cs
        )
    }

    /**
     * flips across the x axis (negates the y value)
     */
    val flipX: Vec2d
        get() = Vec2d(this.x, -this.y)
    /**
     * flips across the y axis (negates the x value)
     */
    val flipY: Vec2d
        get() = Vec2d(-this.x, this.y)

    //=============================================================================

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + x.hashCode()
        result = prime * result + y.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (other == null)
            return false
        return x == (other as Vec2d).x && y == other.y
    }

    override fun toString(): String {
        return "($x,$y)"
    }

    // operators

    operator fun times(other: Vec2d) = this.mul(other)
    operator fun times(other: Double) = this.mul(other)
    operator fun times(other: Float) = this * other.toDouble()
    operator fun times(other: Int) = this * other.toDouble()

    operator fun div(other: Vec2d) = this.divide(other)
    operator fun div(other: Double) = this.divide(other)
    operator fun div(other: Float) = this / other.toDouble()
    operator fun div(other: Int) = this / other.toDouble()

    operator fun plus(other: Vec2d) = this.add(other)
    operator fun minus(other: Vec2d) = this.sub(other)
    operator fun unaryMinus() = this * -1

    operator fun component1() = x
    operator fun component2() = y

    companion object {

        val ZERO = Vec2d(0.0, 0.0)

        fun min(a: Vec2d, b: Vec2d): Vec2d {
            return Vec2d(min(a.x, b.x), min(a.y, b.y))
        }

        fun max(a: Vec2d, b: Vec2d): Vec2d {
            return Vec2d(max(a.x, b.x), max(a.y, b.y))
        }
    }
}

fun vec(x: Number, y: Number) = Vec2d(x.toDouble(), y.toDouble())
