package com.kyant.shapes

import androidx.compose.ui.unit.Density

sealed interface CornerRadius {

    context(density: Density)
    fun toPx(): Float

    data class Px(val value: Float) : CornerRadius {

        context(density: Density)
        override fun toPx(): Float = value
    }

    data class Dp(val value: androidx.compose.ui.unit.Dp) : CornerRadius {

        context(density: Density)
        override fun toPx(): Float = with(density) { value.toPx() }
    }

    data object Zero : CornerRadius {

        context(density: Density)
        override fun toPx(): Float = 0f
    }
}
