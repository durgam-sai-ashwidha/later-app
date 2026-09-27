package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LaterBorder
import com.example.ui.theme.LaterCardBg
import com.example.ui.theme.LaterDarkAccent
import com.example.ui.theme.LaterInkPrimary
import com.example.ui.theme.LaterTerracotta
import com.example.ui.theme.LaterTextMuted
import com.example.ui.theme.LaterTypographyTokens
import com.example.ui.theme.LaterWarmGray
import com.example.ui.theme.LaterWhyBg
import com.example.ui.theme.NewsreaderFamily
import com.example.ui.theme.PlusJakartaSansFamily

/**
 * Capture screen component featuring a multi-line text input focused on the 'WHY'
 * as the primary entry point, styled strictly with the editorial serif typography tokens.
 *
 * Enforces the brand promise:
 * "Give future-you enough context."
 */
@Composable
fun WhyCaptureInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "WHY DOES THIS MATTER?",
    subLabel: String = "— the heart of LATER",
    placeholder: String = "“I need this for next sprint's refactor...” or “Read before exam on Friday...”",
    isPrimaryEntryPoint: Boolean = true,
    minHeight: Dp = 110.dp,
    focusRequester: FocusRequester? = null,
    testTag: String = "capture_why_input"
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Section Header: High-contrast uppercase label in terracotta + editorial subtext
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = label,
                style = LaterTypographyTokens.whyHeaderLabel
            )

            if (subLabel.isNotBlank()) {
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = subLabel,
                    fontFamily = NewsreaderFamily,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = LaterWarmGray
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Visually dominating card container with subtle bookmark-corner motif
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .background(LaterCardBg)
                .border(
                    width = if (isPrimaryEntryPoint) 1.5.dp else 1.dp,
                    color = if (isPrimaryEntryPoint) LaterTerracotta.copy(alpha = 0.6f) else LaterBorder,
                    shape = RoundedCornerShape(6.dp)
                )
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            BookmarkCorner(
                modifier = Modifier.align(Alignment.TopEnd),
                size = 14.dp,
                color = LaterTerracotta
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Editorial quotation mark in Newsreader serif
                Text(
                    text = "“",
                    style = LaterTypographyTokens.whyQuoteMark,
                    modifier = Modifier.padding(end = 6.dp)
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 10.dp)
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            fontFamily = NewsreaderFamily,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Normal,
                            lineHeight = 1.2.em,
                            color = LaterTextMuted
                        )
                    }

                    val inputModifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = minHeight)
                        .testTag(testTag)
                        .let { mod ->
                            if (focusRequester != null) mod.focusRequester(focusRequester) else mod
                        }

                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        textStyle = LaterTypographyTokens.whyEditor,
                        cursorBrush = SolidColor(LaterTerracotta),
                        modifier = inputModifier
                    )
                }
            }
        }
    }
}
