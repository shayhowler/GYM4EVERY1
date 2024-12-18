package com.gym4every1.singletons

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object SignUpViewModel {
    var fullName by mutableStateOf("")
    var email by mutableStateOf("")
    var securityQuestion by mutableStateOf("")
    var answer by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
}