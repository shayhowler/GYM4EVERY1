package com.gym4every1.auth

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth

suspend fun logoutUser(supabaseClient: SupabaseClient) {
    supabaseClient.auth.signOut()
}