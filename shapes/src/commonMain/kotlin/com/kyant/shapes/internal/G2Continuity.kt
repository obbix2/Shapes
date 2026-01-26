package com.kyant.shapes.internal

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.util.fastCoerceAtLeast
import androidx.compose.ui.util.fastCoerceIn
import androidx.compose.ui.util.lerp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

internal data class G2Continuity(
    val profile: G2ContinuityProfile = G2ContinuityProfile.RoundedRectangle,
    val capsuleProfile: G2ContinuityProfile = G2ContinuityProfile.Capsule
) {

    private fun resolveBezier(profile: G2ContinuityProfile) =
        when (profile) {
            this.profile -> this.profile.bezier
            this.capsuleProfile -> this.capsuleProfile.bezier
            else -> profile.bezier
        }

    fun createRoundedRectangleOutline(
        size: Size,
        topLeft: Float,
        topRight: Float,
        bottomRight: Float,
        bottomLeft: Float
    ): Outline {
        val width = size.width
        val height = size.height

        if ((topLeft + topRight == width || topLeft + bottomLeft == height) &&
            (topLeft == topRight && bottomRight == bottomLeft && topLeft == bottomRight)
        ) {
            return when {
                width > height -> Outline.Generic(createHorizontalCapsulePath(width, height))
                width < height -> Outline.Generic(createVerticalCapsulePath(width, height))
                else -> createCircleOutline(width)
            }
        }

        return Outline.Generic(
            createRoundedRectanglePath(
                width = width,
                height = height,
                topLeft = topLeft,
                topRight = topRight,
                bottomRight = bottomRight,
                bottomLeft = bottomLeft
            )
        )
    }

    private fun createRoundedRectanglePath(
        width: Float,
        height: Float,
        topLeft: Float,
        topRight: Float,
        bottomRight: Float,
        bottomLeft: Float
    ): Path {
        val centerX = width * 0.5f
        val centerY = height * 0.5f

        // mnemonics:
        // T: top, R: right, B: bottom, L: left
        // H: horizontal Bezier, V: vertical Bezier, C: arc, I: line

        // non-capsule ratios
        // 0: full capsule, 1: safe rounded rectangle, (0, 1): progressive capsule
        val ratioTLV = ((centerY / topLeft - 1f) / profile.extendedFraction).fastCoerceIn(0f, 1f)
        val ratioTLH = ((centerX / topLeft - 1f) / profile.extendedFraction).fastCoerceIn(0f, 1f)
        val ratioTRH = ((centerX / topRight - 1f) / profile.extendedFraction).fastCoerceIn(0f, 1f)
        val ratioTRV = ((centerY / topRight - 1f) / profile.extendedFraction).fastCoerceIn(0f, 1f)
        val ratioBRV = ((centerY / bottomRight - 1f) / profile.extendedFraction).fastCoerceIn(0f, 1f)
        val ratioBRH = ((centerX / bottomRight - 1f) / profile.extendedFraction).fastCoerceIn(0f, 1f)
        val ratioBLH = ((centerX / bottomLeft - 1f) / profile.extendedFraction).fastCoerceIn(0f, 1f)
        val ratioBLV = ((centerY / bottomLeft - 1f) / profile.extendedFraction).fastCoerceIn(0f, 1f)

        // constrained non-capsule ratios
        val ratioTL = min(ratioTLV, ratioTLH)
        val ratioTR = min(ratioTRH, ratioTRV)
        val ratioBR = min(ratioBRV, ratioBRH)
        val ratioBL = min(ratioBLH, ratioBLV)

        // Bezier stuffs

        // extended fractions
        val extFracTL = lerp(capsuleProfile.extendedFraction, profile.extendedFraction, ratioTL)
        val extFracTR = lerp(capsuleProfile.extendedFraction, profile.extendedFraction, ratioTR)
        val extFracBR = lerp(capsuleProfile.extendedFraction, profile.extendedFraction, ratioBR)
        val extFracBL = lerp(capsuleProfile.extendedFraction, profile.extendedFraction, ratioBL)

        // resolved extended fractions
        val extFracTLV = extFracTL * ratioTLV
        val extFracTLH = extFracTL * ratioTLH
        val extFracTRH = extFracTR * ratioTRH
        val extFracTRV = extFracTR * ratioTRV
        val extFracBRV = extFracBR * ratioBRV
        val extFracBRH = extFracBR * ratioBRH
        val extFracBLH = extFracBL * ratioBLH
        val extFracBLV = extFracBL * ratioBLV

        // offsets
        val offsetTLV = -topLeft * extFracTLV
        val offsetTLH = -topLeft * extFracTLH
        val offsetTRH = -topRight * extFracTRH
        val offsetTRV = -topRight * extFracTRV
        val offsetBRV = -bottomRight * extFracBRV
        val offsetBRH = -bottomRight * extFracBRH
        val offsetBLH = -bottomLeft * extFracBLH
        val offsetBLV = -bottomLeft * extFracBLV

        // Bezier curvature scales
        val bezKScaleTLV = lerp(capsuleProfile.bezierCurvatureScale, profile.bezierCurvatureScale, ratioTLV)
        val bezKScaleTLH = lerp(capsuleProfile.bezierCurvatureScale, profile.bezierCurvatureScale, ratioTLH)
        val bezKScaleTRH = lerp(capsuleProfile.bezierCurvatureScale, profile.bezierCurvatureScale, ratioTRH)
        val bezKScaleTRV = lerp(capsuleProfile.bezierCurvatureScale, profile.bezierCurvatureScale, ratioTRV)
        val bezKScaleBRV = lerp(capsuleProfile.bezierCurvatureScale, profile.bezierCurvatureScale, ratioBRV)
        val bezKScaleBRH = lerp(capsuleProfile.bezierCurvatureScale, profile.bezierCurvatureScale, ratioBRH)
        val bezKScaleBLH = lerp(capsuleProfile.bezierCurvatureScale, profile.bezierCurvatureScale, ratioBLH)
        val bezKScaleBLV = lerp(capsuleProfile.bezierCurvatureScale, profile.bezierCurvatureScale, ratioBLV)

        // arc stuffs

        // arc fractions
        val arcFracTL = lerp(capsuleProfile.arcFraction, profile.arcFraction, ratioTL)
        val arcFracTR = lerp(capsuleProfile.arcFraction, profile.arcFraction, ratioTR)
        val arcFracBR = lerp(capsuleProfile.arcFraction, profile.arcFraction, ratioBR)
        val arcFracBL = lerp(capsuleProfile.arcFraction, profile.arcFraction, ratioBL)

        // arc curvature scales
        val arcKScaleTL = 1f + (profile.arcCurvatureScale - 1f) * ratioTL
        val arcKScaleTR = 1f + (profile.arcCurvatureScale - 1f) * ratioTR
        val arcKScaleBR = 1f + (profile.arcCurvatureScale - 1f) * ratioBR
        val arcKScaleBL = 1f + (profile.arcCurvatureScale - 1f) * ratioBL

        // base Beziers
        val bezierTLV = resolveBezier(G2ContinuityProfile(extFracTLV, arcFracTL, bezKScaleTLV, arcKScaleTL))
        val bezierTLH = resolveBezier(G2ContinuityProfile(extFracTLH, arcFracTL, bezKScaleTLH, arcKScaleTL))
        val bezierTRH = resolveBezier(G2ContinuityProfile(extFracTRH, arcFracTR, bezKScaleTRH, arcKScaleTR))
        val bezierTRV = resolveBezier(G2ContinuityProfile(extFracTRV, arcFracTR, bezKScaleTRV, arcKScaleTR))
        val bezierBRV = resolveBezier(G2ContinuityProfile(extFracBRV, arcFracBR, bezKScaleBRV, arcKScaleBR))
        val bezierBRH = resolveBezier(G2ContinuityProfile(extFracBRH, arcFracBR, bezKScaleBRH, arcKScaleBR))
        val bezierBLH = resolveBezier(G2ContinuityProfile(extFracBLH, arcFracBL, bezKScaleBLH, arcKScaleBL))
        val bezierBLV = resolveBezier(G2ContinuityProfile(extFracBLV, arcFracBL, bezKScaleBLV, arcKScaleBL))

        return Path().apply {
            var x = 0f
            var y = topLeft
            moveTo(x, y - offsetTLV)

            // TL
            if (topLeft > 0f) {
                // TLV
                with(bezierTLV) {
                    cubicTo(
                        x + p1.y * topLeft, y - p1.x * topLeft,
                        x + p2.y * topLeft, y - p2.x * topLeft,
                        x + p3.y * topLeft, y - p3.x * topLeft
                    )
                }

                // TLC
                arcToWithScaledRadius(
                    center = Point(topLeft, topLeft),
                    radius = topLeft,
                    radiusScale = 1f / arcKScaleTL,
                    startAngle = PI.toFloat() + PI.toFloat() * 0.5f * (1f - arcFracTL) * 0.5f,
                    sweepAngle = PI.toFloat() * 0.5f * arcFracTL
                )

                // TLH
                x = topLeft
                y = 0f
                with(bezierTLH) {
                    cubicTo(
                        x - p2.x * topLeft, y + p2.y * topLeft,
                        x - p1.x * topLeft, y + p1.y * topLeft,
                        x - (p0.x * topLeft).fastCoerceAtLeast(offsetTLH), y + p0.y * topLeft
                    )
                }
            }

            // TI
            x = width - topRight
            y = 0f
            lineTo(x + offsetTRH, y)

            // TR
            if (topRight > 0f) {
                // TRH
                with(bezierTRH) {
                    cubicTo(
                        x + p1.x * topRight, y + p1.y * topRight,
                        x + p2.x * topRight, y + p2.y * topRight,
                        x + p3.x * topRight, y + p3.y * topRight
                    )
                }

                // TRC
                arcToWithScaledRadius(
                    center = Point(width - topRight, topRight),
                    radius = topRight,
                    radiusScale = 1f / arcKScaleTR,
                    startAngle = -PI.toFloat() * 0.5f + PI.toFloat() * 0.5f * (1f - arcFracBL) * 0.5f,
                    sweepAngle = PI.toFloat() * 0.5f * arcFracTR
                )

                // TRV
                x = width
                y = topRight
                with(bezierTRV) {
                    cubicTo(
                        x - p2.y * topRight, y - p2.x * topRight,
                        x - p1.y * topRight, y - p1.x * topRight,
                        x - p0.y * topRight, y - (p0.x * topRight).fastCoerceAtLeast(offsetTRV)
                    )
                }
            }

            // RI
            x = width
            y = height - bottomRight
            lineTo(x, y + offsetBRV)

            // BR
            if (bottomRight > 0f) {
                // BRV
                with(bezierBRV) {
                    cubicTo(
                        x - p1.y * bottomRight, y + p1.x * bottomRight,
                        x - p2.y * bottomRight, y + p2.x * bottomRight,
                        x - p3.y * bottomRight, y + p3.x * bottomRight
                    )
                }

                // BRC
                arcToWithScaledRadius(
                    center = Point(width - bottomRight, height - bottomRight),
                    radius = bottomRight,
                    radiusScale = 1f / arcKScaleBR,
                    startAngle = 0f + PI.toFloat() * 0.5f * (1f - arcFracBR) * 0.5f,
                    sweepAngle = PI.toFloat() * 0.5f * arcFracBR
                )

                // BRH
                x = width - bottomRight
                y = height
                with(bezierBRH) {
                    cubicTo(
                        x + p2.x * bottomRight, y - p2.y * bottomRight,
                        x + p1.x * bottomRight, y - p1.y * bottomRight,
                        x + (p0.x * bottomRight).fastCoerceAtLeast(offsetBRH), y - p0.y * bottomRight
                    )
                }
            }

            // BI
            x = bottomLeft
            y = height
            lineTo(x - offsetBLH, y)

            // BL
            if (bottomLeft > 0f) {
                // BLH
                with(bezierBLH) {
                    cubicTo(
                        x - p1.x * bottomLeft, y - p1.y * bottomLeft,
                        x - p2.x * bottomLeft, y - p2.y * bottomLeft,
                        x - p3.x * bottomLeft, y - p3.y * bottomLeft
                    )
                }

                // BLC
                arcToWithScaledRadius(
                    center = Point(bottomLeft, height - bottomLeft),
                    radius = bottomLeft,
                    radiusScale = 1f / arcKScaleBL,
                    startAngle = PI.toFloat() * 0.5f + PI.toFloat() * 0.5f * (1f - arcFracBL) * 0.5f,
                    sweepAngle = PI.toFloat() * 0.5f * arcFracBL
                )

                // BLV
                x = 0f
                y = height - bottomLeft
                with(bezierBLV) {
                    cubicTo(
                        x + p2.y * bottomLeft, y + p2.x * bottomLeft,
                        x + p1.y * bottomLeft, y + p1.x * bottomLeft,
                        x + p0.y * bottomLeft, y + (p0.x * bottomLeft).fastCoerceAtLeast(offsetBLV)
                    )
                }
            }

            // LI
            close()
        }
    }

    private fun createHorizontalCapsulePath(width: Float, height: Float): Path {
        val radius = height * 0.5f
        val centerX = width * 0.5f

        val ratioH = ((centerX / radius - 1f) / capsuleProfile.extendedFraction).fastCoerceIn(0f, 1f)
        val extFrac = capsuleProfile.extendedFraction
        val extFracH = extFrac * ratioH
        val offsetH = -radius * extFracH
        val bezKScaleH = lerp(capsuleProfile.bezierCurvatureScale, profile.bezierCurvatureScale, ratioH)
        val arcFrac = capsuleProfile.arcFraction
        val bezierH =
            resolveBezier(
                G2ContinuityProfile(
                    extendedFraction = extFracH,
                    arcFraction = arcFrac,
                    bezierCurvatureScale = bezKScaleH,
                    arcCurvatureScale = 1f
                )
            ) * radius

        val arcRad = PI.toFloat() * 0.5f * arcFrac
        val bezRad = (PI.toFloat() * 0.5f - arcRad) * 0.5f
        val sweepRad = (bezRad + arcRad) * 2f

        return Path().apply {
            // LC
            arcTo(
                center = Point(radius, radius),
                radius = radius,
                startAngle = PI.toFloat() * 0.5f + bezRad,
                sweepAngle = sweepRad
            )

            // TLH
            var x = radius
            var y = 0f
            with(bezierH) {
                cubicTo(
                    x - p2.x, y + p2.y,
                    x - p1.x, y + p1.y,
                    x - p0.x.fastCoerceAtLeast(offsetH), y + p0.y
                )
            }

            // TI
            x = width - radius
            y = 0f
            lineTo(x + offsetH, y)

            // TRH
            with(bezierH) {
                cubicTo(
                    x + p1.x, y + p1.y,
                    x + p2.x, y + p2.y,
                    x + p3.x, y + p3.y
                )
            }

            // RC
            arcTo(
                center = Point(width - radius, radius),
                radius = radius,
                startAngle = -(PI.toFloat() * 0.5f - bezRad),
                sweepAngle = sweepRad
            )

            // BRH
            x = width - radius
            y = height
            with(bezierH) {
                cubicTo(
                    x + p2.x, y - p2.y,
                    x + p1.x, y - p1.y,
                    x + p0.x.fastCoerceAtLeast(offsetH), y - p0.y
                )
            }

            // BI
            x = radius
            y = height
            lineTo(x - offsetH, y)

            // BLH
            with(bezierH) {
                cubicTo(
                    x - p1.x, y - p1.y,
                    x - p2.x, y - p2.y,
                    x - p3.x, y - p3.y
                )
            }
        }
    }

    private fun createVerticalCapsulePath(width: Float, height: Float): Path {
        val radius = width * 0.5f
        val centerY = height * 0.5f

        val ratioV = ((centerY / radius - 1f) / capsuleProfile.extendedFraction).fastCoerceIn(0f, 1f)
        val extFrac = capsuleProfile.extendedFraction
        val extFracV = extFrac * ratioV
        val offsetV = -radius * extFracV
        val bezKScaleV = lerp(capsuleProfile.bezierCurvatureScale, profile.bezierCurvatureScale, ratioV)
        val arcFrac = capsuleProfile.arcFraction
        val bezierV =
            resolveBezier(
                G2ContinuityProfile(
                    extendedFraction = extFracV,
                    arcFraction = arcFrac,
                    bezierCurvatureScale = bezKScaleV,
                    arcCurvatureScale = 1f
                )
            ) * radius

        val arcRad = PI.toFloat() * 0.5f * arcFrac
        val bezRad = (PI.toFloat() * 0.5f - arcRad) * 0.5f
        val sweepRad = (bezRad + arcRad) * 2f

        return Path().apply {
            var x = 0f
            var y = radius
            moveTo(x, y - offsetV)

            // TLV
            with(bezierV) {
                cubicTo(
                    x + p1.y, y - p1.x,
                    x + p2.y, y - p2.x,
                    x + p3.y, y - p3.x
                )
            }

            // TC
            arcTo(
                center = Point(radius, radius),
                radius = radius,
                startAngle = -(PI.toFloat() - bezRad),
                sweepAngle = sweepRad
            )

            // TRV
            x = width
            y = radius
            with(bezierV) {
                cubicTo(
                    x - p2.y, y - p2.x,
                    x - p1.y, y - p1.x,
                    x - p0.y, y - p0.x.fastCoerceAtLeast(offsetV)
                )
            }

            // RI
            x = width
            y = height - radius
            lineTo(x, y + offsetV)

            // BRV
            with(bezierV) {
                cubicTo(
                    x - p1.y, y + p1.x,
                    x - p2.y, y + p2.x,
                    x - p3.y, y + p3.x
                )
            }

            // BC
            arcTo(
                center = Point(width - radius, height - radius),
                radius = radius,
                startAngle = bezRad,
                sweepAngle = sweepRad
            )

            // BLV
            x = 0f
            y = height - radius
            with(bezierV) {
                cubicTo(
                    x + p2.y, y + p2.x,
                    x + p1.y, y + p1.x,
                    x + p0.y, y + p0.x.fastCoerceAtLeast(offsetV)
                )
            }

            // LI
            close()
        }
    }

    companion object {

        val Default = G2Continuity()
    }
}

private fun createCircleOutline(size: Float): Outline {
    val radius = size * 0.5f
    return Outline.Rounded(
        RoundRect(
            rect = Rect(0f, 0f, size, size),
            radiusX = radius,
            radiusY = radius
        )
    )
}

private fun Path.arcToWithScaledRadius(
    center: Point,
    radius: Float,
    radiusScale: Float,
    startAngle: Float,
    sweepAngle: Float
) {
    val centerAngle = startAngle + sweepAngle * 0.5f
    return arcTo(
        center = center + Point(cos(centerAngle), sin(centerAngle)) * radius * (1f - radiusScale),
        radius = radius * radiusScale,
        startAngle = startAngle,
        sweepAngle = sweepAngle
    )
}

private fun Path.arcTo(
    center: Point,
    radius: Float,
    startAngle: Float,
    sweepAngle: Float
) {
    arcTo(
        rect = Rect(
            left = center.x - radius,
            top = center.y - radius,
            right = center.x + radius,
            bottom = center.y + radius
        ),
        startAngleDegrees = startAngle * (180f / PI.toFloat()),
        sweepAngleDegrees = sweepAngle * (180f / PI.toFloat()),
        forceMoveTo = false
    )
}
