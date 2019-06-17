package dev.thecodewarrior.avatar.dsl

import dev.thecodewarrior.avatar.util.Vec2d
import dev.thecodewarrior.avatar.util.vec

/**
 * Docs for paths are taken from [MDN](https://developer.mozilla.org/en-US/docs/Web/SVG/Attribute/d#Path_commands)
 *
 * Path starts out using absolute coordinates
 */
class Path {
    var pos: Vec2d = vec(0, 0)
    private val commands = mutableListOf<String>()
    private var absolute = true
    private var startPos: Vec2d? = pos

    private fun addCommand(name: Char, vararg parameters: Double) {
        val command = if (absolute)
            name.toUpperCase()
        else
            name.toLowerCase()
        commands.add("$command ${parameters.joinToString(" ")}")
    }

    private fun newPos(new: Vec2d) {
        if(absolute)
            pos = new
        else
            pos += new
    }

    private fun startCurve() {
        if(startPos == null) startPos = pos
    }

    private fun endCurve() {
        startPos = null
    }

    /**
     * Starts using absolute coordinates
     */
    fun abs() = build {
        absolute = true
    }

    /**
     * Starts using relative coordinates
     */
    fun rel() = build {
        absolute = false
    }

    /**
     * _MoveTo_ instructions can be thought of as picking up the drawing instrument, and setting it down somewhere else,
     * i.e. moving the current point. There is no line drawn between the last current point and the new _current point_.
     *
     * **Absolute:**
     *
     * Move the _current_ point to [end]
     *
     * **Relative:**
     *
     * Move the current point by shifting the last known position of the path by [end]
     */
    fun move(end: Vec2d) = build {
        endCurve()
        addCommand('M', end.x, end.y)
        newPos(end)
    }

    /**
     * _LineTo_ instructions draw a straight line from the _current point_ to the end point, based on the parameters
     * specified. The end point then becomes the current point for the next command.
     *
     * **Absolute:**
     *
     * Draw a line from the _current point_ to the _end point_ [end].
     *
     * **Relative:**
     *
     * Draw a line from the _current point_ to the _end point_, which is the _current point_ shifted by [end].
     */
    fun line(end: Vec2d) = build {
        startCurve()
        addCommand('L', end.x, end.y)
        newPos(end)
    }

    /**
     * _LineTo_ instructions draw a straight line from the _current point_ to the end point, based on the parameters
     * specified. The end point then becomes the current point for the next command.
     *
     * **Absolute:**
     *
     * Draw a line from the _current point_ to the _end point_, which is the [x] parameter and the _current point's_
     * y coordinate.
     *
     * **Relative:**
     *
     * Draw a line from the _current point_ to the _end point_, which is the _current point's_ shifted by the [x]
     * parameter and the _current point's_ y coordinate.
     */
    fun horizontalLine(x: Double) = build {
        startCurve()
        addCommand('H', x)
        if(absolute)
            pos = vec(x, pos.y)
        else
            pos += vec(x, 0)
    }

    /**
     * _LineTo_ instructions draw a straight line from the _current point_ to the end point, based on the parameters
     * specified. The end point then becomes the current point for the next command.
     *
     * **Absolute:**
     *
     * Draw a line from the _current point_ to the _end point_, which is the [y] parameter and the _current point's_
     * x coordinate.
     *
     * **Relative:**
     *
     * Draw a line from the _current point_ to the _end point_, which is the _current point's_ shifted by the [y]
     * parameter and the _current point's_ x coordinate.
     */
    fun verticalLine(y: Double) = build {
        startCurve()
        addCommand('V', y)
        if(absolute)
            pos = vec(pos.x, y)
        else
            pos += vec(0, y)
    }

    /**
     * Cubic Bézier curves are smooth curve definitions using four points:
     *
     * - starting point (current point)
     * - end point
     * - start control point (controls curvature near the start of the curve)
     * - end control point (controls curvature near the end of the curve)
     *
     * After drawing, the _end point_ becomes the _current point_ for the next command.
     *
     * **Absolute:**
     *
     * Draw a cubic Bézier curve from the _current point_ to the _end point_ specified by [end]. The _start control
     * point_ is specified by [startControl] and the _end control point_ is specified by [endControl].
     *
     * **Relative:**
     *
     * Draw a cubic Bézier curve from the _current point_ to the _end point_, which is the _current point_ shifted by
     * [end]. The _start control point_ is the _current point_ (starting point of the curve) shifted by [startControl].
     * The _end control point_ is the _current point_ (starting point of the curve) shifted by [endControl].
     */
    fun cubic(startControl: Vec2d, endControl: Vec2d, end: Vec2d) = build {
        startCurve()
        addCommand('C', startControl.x, startControl.y, endControl.x, endControl.y, end.x, end.y)
        newPos(end)
    }

    /**
     * Cubic Bézier curves are smooth curve definitions using four points:
     *
     * - starting point (current point)
     * - end point
     * - start control point (controls curvature near the start of the curve)
     * - end control point (controls curvature near the end of the curve)
     *
     * After drawing, the _end point_ becomes the _current point_ for the next command.
     *
     * **Absolute:**
     *
     * Draw a cubic Bézier curve from the _current point_ to the _end point_ specified by [end]. The _end control
     * point_ is specified by [endControl]. The _start control point_ is a reflection of the _end control point_ of
     * the previous curve command. If the previous command wasn't a cubic bézier curve, the _start control point_ is
     * the same as the curve starting point (_current point_).
     *
     * **Relative:**
     *
     * Draw a cubic Bézier curve from the _current point_ to the _end point_, which is the _current point_ shifted
     * by [end]. The _end control point_ is the _current point_ (starting point of the curve) shifted by [endControl].
     * The _start control point_ is a reflection of the _end control point_ of the previous curve command. If the
     * previous command wasn't a cubic bézier curve, the _start control point_ is the same as the curve starting
     * point (_current point_).
     */
    fun smoothCubic(endControl: Vec2d, end: Vec2d) = build {
        startCurve()
        addCommand('S', endControl.x, endControl.y, end.x, end.y)
        newPos(end)
    }

    /**
     * Quadratic Bézier curves are smooth curve definitions using three points:
     *
     * - starting point (_current point_)
     * - end point
     * - control point (controls curvature)
     *
     * After drawing, the _end point_ becomes the _current point_ for the next command.
     *
     * **Absolute:**
     *
     * Draw a quadratic Bézier curve from the _current point_ to the _end point_ specified by [end]. The control point
     * is specified by [control].
     *
     * **Relative:**
     *
     * Draw a quadratic Bézier curve from the _current point_ to the _end point_, which is the _current point_ shifted
     * by [end]. The _control point_ is the _current point_ (starting point of the curve) shifted by [control].
     */
    fun quad(control: Vec2d, end: Vec2d) = build {
        startCurve()
        addCommand('S', control.x, control.y, end.x, end.y)
        newPos(end)
    }

    /**
     * Quadratic Bézier curves are smooth curve definitions using three points:
     *
     * - starting point (_current point_)
     * - end point
     * - control point (controls curvature)
     *
     * After drawing, the _end point_ becomes the _current point_ for the next command.
     *
     * **Absolute:**
     *
     * Draw a smooth quadratic Bézier curve from the _current point_ to the _end point_ specified by [end]. The
     * _control point_ is a reflection of the _control point_ of the previous curve command. If the previous command
     * wasn't a quadratic bézier curve, the _control point_ is the same as the curve starting point (_current point_).
     *
     * **Relative:**
     *
     * Draw a smooth quadratic Bézier curve from the _current point_ to the _end point_, which is the _current point_
     * shifted by [end]. The _control point_ is a reflection of the _control point_ of the previous curve command.
     * If the previous command wasn't a quadratic bézier curve, the _control point_ is the same as the curve starting
     * point (_current point_).
     */
    fun smoothQuad(end: Vec2d) = build {
        startCurve()
        addCommand('T', end.x, end.y)
    }

    /**
     * Elliptical arc curves are curves defined as a portion of an ellipse. It is sometimes easier to draw highly
     * regular curves with an elliptical arc than with a Bézier curve.
     *
     * The shape of the ellipse is specified with these additional parameters:
     * - [radiusX] and [radiusY] are the two radii of the ellipse
     * - [angle] represents a rotation (in degrees) of the ellipse relative to the x-axis
     * - [largeArc] and [clockwise] allows to chose which arc must be drawn as 4 possible arcs can be drawn out of the
     * other parameters
     *     * [largeArc] allows to chose one of the large arc or small arc
     *     * [clockwise] allows to chose one of the clockwise turning arc or anticlockwise turning arc
     *
     * After drawing, the _end point_ becomes the _current point_ for the next command.
     *
     * **Absolute:**
     *
     * Draw an Arc curve from the _current point_ to the coordinate [end]. The center of the ellipse used to draw the
     * arc is determined automatically based on the other parameters of the command.
     *
     * **Relative:**
     *
     * Draw an Arc curve from the _current point_ to the _end point_, which is the _current point_ shifted by [end].
     * The center of the ellipse used to draw the arc is determined automatically based on the other parameters of
     * the command.
     */
    fun arc(radiusX: Double, radiusY: Double, angle: Double, largeArc: Boolean, clockwise: Boolean, end: Vec2d) = build {
        startCurve()
        addCommand('A', radiusX, radiusY, angle, if(largeArc) 1.0 else 0.0, if(clockwise) 1.0 else 0.0, end.x, end.y)
        newPos(end)
    }

    /**
     * ClosePath instructions draw a straight line from the current position, to the first point in the path.
     *
     * **Absolute or Relative:**
     *
     * Close the current subpath by connecting the last point of the path with its initial point. If the two points
     * are at different coordinates, a straight line is drawn between those two points.
     *
     * **Note:**
     *
     * The appearance of a shape closed with closepath may be different to that of one closed by drawing a line to the
     * origin, using one of the other commands, because the line ends are joined together
     * (according to the `stroke-linejoin` setting), rather than just being placed at the same coordinates.
     */
    fun closePath() = build {
        addCommand('Z')
        startPos?.also {
            pos = it
        }
        endCurve()
    }

    fun build(): String {
        return commands.joinToString(" ")
    }

    private inline fun build(callback: () -> Unit): Path {
        callback()
        return this
    }

    companion object {
        fun build() = Path()

        inline fun build(callback: Path.() -> Unit): String {
            val path = Path()
            path.callback()
            return path.build()
        }
    }
}

inline fun path(callback: Path.() -> Unit): String = Path.build(callback)
