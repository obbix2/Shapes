package com.kyant.shapes

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.util.fastCoerceIn
import com.kyant.shapes.internal.G2Continuity

sealed interface RoundedCornerStyle {

    fun createOutline(
        size: Size,
        topLeft: Float,
        topRight: Float,
        bottomRight: Float,
        bottomLeft: Float
    ): Outline

    data object Circular : RoundedCornerStyle {

        override fun createOutline(
            size: Size,
            topLeft: Float,
            topRight: Float,
            bottomRight: Float,
            bottomLeft: Float
        ): Outline {
            val width = size.width
            val height = size.height
            val maxRadius = size.minDimension * 0.5f
            return Outline.Rounded(
                RoundRect(
                    left = 0f,
                    top = 0f,
                    right = width,
                    bottom = height,
                    topLeftCornerRadius = CornerRadius(topLeft.fastCoerceIn(0f, maxRadius)),
                    topRightCornerRadius = CornerRadius(topRight.fastCoerceIn(0f, maxRadius)),
                    bottomRightCornerRadius = CornerRadius(bottomRight.fastCoerceIn(0f, maxRadius)),
                    bottomLeftCornerRadius = CornerRadius(bottomLeft.fastCoerceIn(0f, maxRadius))
                )
            )
        }
    }

    data object Continuous : RoundedCornerStyle {

        override fun createOutline(
            size: Size,
            topLeft: Float,
            topRight: Float,
            bottomRight: Float,
            bottomLeft: Float
        ): Outline {
            return G2Continuity.Default.createRoundedRectangleOutline(size, topLeft, topRight, bottomRight, bottomLeft)
        }
    }
}
