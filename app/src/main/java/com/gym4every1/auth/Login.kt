package com.gym4every1.auth

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email

suspend fun loginUser(supabaseClient: SupabaseClient, userEmail: String, userPassword: String) {
    supabaseClient.auth.signInWith(Email){
        email = userEmail
        password = userPassword
    }
}