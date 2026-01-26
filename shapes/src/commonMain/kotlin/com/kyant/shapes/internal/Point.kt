package com.kyant.shapes.internal

internal data class Point(val x: Float, val y: Float) {

    operator fun unaryMinus(): Point {
        return Point(-x, -y)
    }

    operator fun minus(other: Point): Point {
        return Point(x - other.x, y - other.y)
    }

    operator fun plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }

    operator fun times(operand: Float): Point {
        return Point(x * operand, y * operand)
    }

    operator fun div(operand: Float): Point {
        return Point(x / operand, y / operand)
    }
}
