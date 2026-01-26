package com.kyant.shapes

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@Suppress("FunctionName")
fun RoundedRectangle(
    topStart: CornerRadius = CornerRadius.Zero,
    topEnd: CornerRadius = CornerRadius.Zero,
    bottomEnd: CornerRadius = CornerRadius.Zero,
    bottomStart: CornerRadius = CornerRadius.Zero,
    rtlAware: Boolean = true,
    style: RoundedCornerStyle = RoundedCornerStyle.Continuous
) =
    UnevenRoundedRectangle(
        cornerRadii = RectangleCornerRadii(
            topStart = topStart,
            topEnd = topEnd,
            bottomEnd = bottomEnd,
            bottomStart = bottomStart,
            rtlAware = rtlAware
        ),
        style = style
    )

@Suppress("FunctionName")
fun RoundedRectangle(
    topStart: Float = 0f,
    topEnd: Float = 0f,
    bottomEnd: Float = 0f,
    bottomStart: Float = 0f,
    rtlAware: Boolean = true,
    style: RoundedCornerStyle = RoundedCornerStyle.Continuous
) =
    UnevenRoundedRectangle(
        cornerRadii = RectangleCornerRadii(
            topStart = CornerRadius.Px(topStart),
            topEnd = CornerRadius.Px(topEnd),
            bottomEnd = CornerRadius.Px(bottomEnd),
            bottomStart = CornerRadius.Px(bottomStart),
            rtlAware = rtlAware
        ),
        style = style
    )

@Suppress("FunctionName")
fun RoundedRectangle(
    topStart: Dp = 0f.dp,
    topEnd: Dp = 0f.dp,
    bottomEnd: Dp = 0f.dp,
    bottomStart: Dp = 0f.dp,
    rtlAware: Boolean = true,
    style: RoundedCornerStyle = RoundedCornerStyle.Continuous
) =
    UnevenRoundedRectangle(
        cornerRadii = RectangleCornerRadii(
            topStart = CornerRadius.Dp(topStart),
            topEnd = CornerRadius.Dp(topEnd),
            bottomEnd = CornerRadius.Dp(bottomEnd),
            bottomStart = CornerRadius.Dp(bottomStart),
            rtlAware = rtlAware
        ),
        style = style
    )

data class UnevenRoundedRectangle(
    val cornerRadii: RectangleCornerRadii,
    val style: RoundedCornerStyle = RoundedCornerStyle.Continuous
) : RoundedRectangularShape {

    override fun cornerRadii(size: Size, layoutDirection: LayoutDirection, density: Density): FloatArray {
        return context(layoutDirection, density) {
            floatArrayOf(
                cornerRadii.topLeft,
                cornerRadii.topRight,
                cornerRadii.bottomRight,
                cornerRadii.bottomLeft
            )
        }
    }

    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        return context(layoutDirection, density) {
            style.createOutline(
                size = size,
                topLeft = cornerRadii.topLeft,
                topRight = cornerRadii.topRight,
                bottomRight = cornerRadii.bottomRight,
                bottomLeft = cornerRadii.bottomLeft
            )
        }
    }

    fun copy(
        topStart: CornerRadius = cornerRadii.topStart,
        topEnd: CornerRadius = cornerRadii.topEnd,
        bottomEnd: CornerRadius = cornerRadii.bottomEnd,
        bottomStart: CornerRadius = cornerRadii.bottomStart,
        rtlAware: Boolean = cornerRadii.rtlAware,
        style: RoundedCornerStyle = this.style
    ) =
        UnevenRoundedRectangle(
            cornerRadii = RectangleCornerRadii(
                topStart = topStart,
                topEnd = topEnd,
                bottomEnd = bottomEnd,
                bottomStart = bottomStart,
                rtlAware = rtlAware
            ),
            style = style
        )
}
