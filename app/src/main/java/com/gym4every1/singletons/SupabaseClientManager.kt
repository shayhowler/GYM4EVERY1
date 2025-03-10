package com.gym4every1.singletons

import com.gym4every1.BuildConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage

object SupabaseClientManager {
    private var _supabaseClient: SupabaseClient? = null

    // Initialize SupabaseClient if it hasn't been initialized yet
    fun getSupabaseClient(): SupabaseClient {
        if (_supabaseClient == null) {
            _supabaseClient = createSupabaseClient(
                supabaseUrl = BuildConfig.SUPABASE_URL,
                supabaseKey = BuildConfig.SUPABASE_ANON_KEY
            ) {
                install(Auth) {
                    flowType = io.github.jan.supabase.auth.FlowType.PKCE
                }
                install(Postgrest)
                install(Storage)
                install(Realtime)
            }
        }
        return _supabaseClient!!
    }
}
