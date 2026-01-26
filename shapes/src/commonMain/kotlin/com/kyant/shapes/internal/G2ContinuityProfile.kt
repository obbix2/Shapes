package com.kyant.shapes.internal

import androidx.compose.ui.util.fastCoerceAtLeast
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

internal data class G2ContinuityProfile(
    val extendedFraction: Float,
    val arcFraction: Float,
    val bezierCurvatureScale: Float,
    val arcCurvatureScale: Float
) {

    private var _bezier: CubicBezier? = null

    val bezier: CubicBezier
        get() = _bezier ?: createBaseBezier().also { _bezier = it }

    private fun createBaseBezier(): CubicBezier {
        val arcRadians = PI.toFloat() * 0.5f * arcFraction
        val bezierRadians = (PI.toFloat() * 0.5f - arcRadians) * 0.5f
        val sin = sin(bezierRadians)
        val cos = cos(bezierRadians)
        val radiusScale = 1f / arcCurvatureScale
        val arcCenter = Point(0f, 1f) + Point(1f / sqrt(2f), -1f / sqrt(2f)) * (1f - radiusScale)
        val arcStartPoint = arcCenter + Point(sin, -cos) * radiusScale
        return generateG2ContinuousBezierWithZeroStartCurvature(
            start = Point(-extendedFraction, 0f),
            end = arcStartPoint,
            startTangent = Point(1f, 0f),
            endTangent = Point(cos, sin),
            endCurvature = bezierCurvatureScale
        )
    }

    companion object {

        val RoundedRectangle: G2ContinuityProfile =
            G2ContinuityProfile(
                extendedFraction = 0.5286651f,
                arcFraction = 5f / 9f,
                bezierCurvatureScale = 1.0732051f,
                arcCurvatureScale = 1.0732051f
            )

        val Capsule: G2ContinuityProfile =
            G2ContinuityProfile(
                extendedFraction = 0.5286651f * 0.75f,
                arcFraction = 0f,
                bezierCurvatureScale = 1f,
                arcCurvatureScale = 1f
            )

        val G1Equivalent: G2ContinuityProfile =
            G2ContinuityProfile(
                extendedFraction = 0f,
                arcFraction = 1f,
                bezierCurvatureScale = 1f,
                arcCurvatureScale = 1f
            )
    }
}

private fun generateG2ContinuousBezierWithZeroStartCurvature(
    start: Point,
    end: Point,
    startTangent: Point,
    endTangent: Point,
    endCurvature: Float
): CubicBezier {
    val a2 = 1.5f * endCurvature
    val b = startTangent.x * endTangent.y - startTangent.y * endTangent.x
    val dx = end.x - start.x
    val dy = end.y - start.y
    val c1 = -dy * startTangent.x + dx * startTangent.y
    val c2 = dy * endTangent.x - dx * endTangent.y
    val lambda0 = -c2 / b - a2 * c1 * c1 / b / b / b
    val lambda3 = -c1 / b
    return CubicBezier(
        start,
        start + Point(
            (lambda0 * startTangent.x).fastCoerceAtLeast(0f),
            (lambda0 * startTangent.y).fastCoerceAtLeast(0f)
        ),
        end - Point(
            (lambda3 * endTangent.x).fastCoerceAtLeast(0f),
            (lambda3 * endTangent.y).fastCoerceAtLeast(0f)
        ),
        end
    )
}
