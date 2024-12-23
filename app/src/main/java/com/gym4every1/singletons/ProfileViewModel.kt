package com.gym4every1.singletons

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object ProfileViewModel {
    var userHeight by mutableIntStateOf(170)
    var userWeight by mutableIntStateOf(80)
    var userDateOfBirth by mutableStateOf<String?>(null)
    var activityLevel by mutableIntStateOf(1)
    var goal by mutableIntStateOf(1)

    // Function to clear all data
    fun clear() {
        userHeight = 170
        userWeight = 80
        userDateOfBirth = null
        activityLevel = 1
        goal = 1
    }
}