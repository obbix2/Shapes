package com.kyant.shapes

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

fun RoundedRectangle(
    cornerRadius: Float,
    style: RoundedCornerStyle = RoundedCornerStyle.Continuous
) =
    RoundedRectangle(
        cornerRadius = CornerRadius.Px(cornerRadius),
        style = style
    )

fun RoundedRectangle(
    cornerRadius: Dp,
    style: RoundedCornerStyle = RoundedCornerStyle.Continuous
) =
    RoundedRectangle(
        cornerRadius = CornerRadius.Dp(cornerRadius),
        style = style
    )

data class RoundedRectangle(
    val cornerRadius: CornerRadius,
    val style: RoundedCornerStyle = RoundedCornerStyle.Continuous
) : RoundedRectangularShape {

    override fun cornerRadii(size: Size, layoutDirection: LayoutDirection, density: Density): FloatArray {
        val cornerRadiusPx = context(density) { cornerRadius.toPx() }
        return FloatArray(4) { cornerRadiusPx }
    }

    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val cornerRadiusPx = context(density) { cornerRadius.toPx() }

        if (cornerRadiusPx <= 0f) {
            return Outline.Rectangle(
                Rect(
                    left = 0f,
                    top = 0f,
                    right = size.width,
                    bottom = size.height
                )
            )
        }

        return style.createOutline(
            size = size,
            topLeft = cornerRadiusPx,
            topRight = cornerRadiusPx,
            bottomRight = cornerRadiusPx,
            bottomLeft = cornerRadiusPx
        )
    }

    fun copy(
        cornerRadius: Float,
        style: RoundedCornerStyle = this.style
    ) =
        RoundedRectangle(
            cornerRadius = cornerRadius,
            style = style
        )

    fun copy(
        cornerRadius: Dp,
        style: RoundedCornerStyle = this.style
    ) =
        RoundedRectangle(
            cornerRadius = cornerRadius,
            style = style
        )

    fun asUneven(rtlAware: Boolean = true): UnevenRoundedRectangle {
        return UnevenRoundedRectangle(
            cornerRadii = RectangleCornerRadii(
                topStart = cornerRadius,
                topEnd = cornerRadius,
                bottomEnd = cornerRadius,
                bottomStart = cornerRadius,
                rtlAware = rtlAware
            ),
            style = style
        )
    }
}
