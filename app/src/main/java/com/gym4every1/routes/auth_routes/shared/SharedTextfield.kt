package com.gym4every1.routes.auth_routes.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import com.gym4every1.R

@Composable
fun CustomTextFieldWithIcon(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit, // Accepts a composable for the icon
    isPassword: Boolean = false,
    isError: Boolean = false
) {
    // State to track whether password is visible
    var passwordVisible = remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .width(300.dp)
            .padding(bottom = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = if (isError) Color(0xFFD32F2F) else Color(0xFFED4747),
                    shape = RoundedCornerShape(30.dp)
                )
                .height(48.dp)
                .background(Color.Transparent),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // Render the passed icon composable on the left side
                icon()

                // The TextField itself
                TextField(
                    value = value,
                    onValueChange = onValueChange,
                    visualTransformation = if (isPassword && !passwordVisible.value) PasswordVisualTransformation() else VisualTransformation.None,
                    placeholder = {
                        Text(
                            text = placeholder,
                            fontFamily = FontFamily(Font(R.font.lato)),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 1.sp,
                            color = Color(0xFFED4747),
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color(0xFFED4747),
                        unfocusedTextColor = Color(0xFFED4747),
                        disabledTextColor = Color.Gray,
                        errorTextColor = Color(0xFFD32F2F),
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent,
                        cursorColor = Color(0xFFED4747),
                        errorCursorColor = Color(0xFFD32F2F),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(Color.Transparent)
                )
            }

            // Icon button to toggle password visibility at the end
            if (isPassword) {
                IconButton(
                    onClick = { passwordVisible.value = !passwordVisible.value },
                    modifier = Modifier
                        .align(Alignment.CenterEnd) // Aligns the icon at the end of the field
                ) {
                    FaIcon(
                        faIcon = if (passwordVisible.value) FaIcons.EyeSlash else FaIcons.Eye,
                        modifier = Modifier.size(30.dp)
                            .offset(y = 5.dp),
                        tint = Color.Black // Set tint for visibility
                    )
                }
            }
        }
    }
}