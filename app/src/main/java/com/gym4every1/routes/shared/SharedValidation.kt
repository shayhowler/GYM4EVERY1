package com.gym4every1.routes.shared

import android.util.Patterns

fun isValidEmail(email: String): Boolean {
    return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun validateFields(
    fullName: String,
    email: String,
    password: String,
    confirmPassword: String,
    isSignIn: Boolean
): String {
    return if (isSignIn) {
        when {
            email.isEmpty() -> "Email is required"
            password.isEmpty() -> "Password is required"
            else -> "" // No error
        }
    } else {
        when {
            fullName.isEmpty() -> "Name is required"
            email.isEmpty() -> "Email is required"
            password.isEmpty() -> "Password is required"
            confirmPassword.isEmpty() -> "Confirm Password is required"
            password != confirmPassword -> "Passwords do not match"
            else -> "" // No error
        }
    }
}