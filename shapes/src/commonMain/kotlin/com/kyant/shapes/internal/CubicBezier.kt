package com.kyant.shapes.internal

internal data class CubicBezier(
    val p0: Point,
    val p1: Point,
    val p2: Point,
    val p3: Point
) {

    operator fun times(operand: Float): CubicBezier {
        return CubicBezier(
            p0 * operand,
            p1 * operand,
            p2 * operand,
            p3 * operand
        )
    }
}
