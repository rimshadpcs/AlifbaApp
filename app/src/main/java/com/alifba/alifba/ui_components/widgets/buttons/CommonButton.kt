package com.alifba.alifba.ui_components.widgets.buttons

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alifba.alifba.R
import com.alifba.alifba.ui_components.theme.darkPurple
import com.alifba.alifba.ui_components.theme.darkYellow
import com.alifba.alifba.ui_components.theme.lightPurple
import com.alifba.alifba.ui_components.theme.white
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CommonButton(
    onClick: () -> Unit,
    buttonText: String,
    shadowColor: Color,
    mainColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier // Add the modifier parameter here with a default value
) {
    val alifbaFont = FontFamily(
        Font(R.font.more_sugar_regular, FontWeight.SemiBold)
    )
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val coroutineScope = rememberCoroutineScope()

    // Animate the offset for the press effect
    val offsetY by animateDpAsState(
        targetValue = if (isPressed) 0.dp else 5.dp,
        animationSpec = spring(), label = ""
    )

    Box(
        modifier = modifier // Use the modifier passed to the function
            .fillMaxWidth()
            .padding(8.dp)
            .height(60.dp) // Total height including the shadow
            .clip(RoundedCornerShape(32.dp)),
        contentAlignment = Alignment.TopCenter
    ) {
        // Shadow layer
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(shadowColor) // Shadow color
        )

        // Button face layer
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(bottom = offsetY) // Apply the offset for the press effect
                .clip(RoundedCornerShape(32.dp))
                .background(mainColor) // Button face color
                .clickable(
                    onClick = {
                        coroutineScope.launch {
                            delay(100)
                            onClick()
                        }
                    },
                    interactionSource = interactionSource,
                    indication = null
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = buttonText,
                fontFamily = alifbaFont,
                color = textColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCommonButton() {
    CommonButton(
        onClick = {},
        buttonText = "Next",
        mainColor = lightPurple,
        shadowColor = darkPurple,
        textColor = white
    )
}
