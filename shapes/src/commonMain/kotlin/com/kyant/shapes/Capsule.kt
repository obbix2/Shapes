package com.kyant.shapes

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

val Capsule = Capsule()

@Suppress("FunctionName")
fun Capsule(
    style: RoundedCornerStyle = RoundedCornerStyle.Continuous
) =
    CapsuleShape(style = style)

@ExposedCopyVisibility
data class CapsuleShape internal constructor(
    val style: RoundedCornerStyle = RoundedCornerStyle.Continuous
) : RoundedRectangularShape {

    override fun cornerRadii(size: Size, layoutDirection: LayoutDirection, density: Density): FloatArray {
        val cornerRadiusPx = size.minDimension * 0.5f
        return FloatArray(4) { cornerRadiusPx }
    }

    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val cornerRadiusPx = size.minDimension * 0.5f
        return style.createOutline(
            size = size,
            topLeft = cornerRadiusPx,
            topRight = cornerRadiusPx,
            bottomRight = cornerRadiusPx,
            bottomLeft = cornerRadiusPx
        )
    }
}
