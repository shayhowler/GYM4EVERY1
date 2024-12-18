package com.gym4every1.singletons

import android.content.Context
import com.gym4every1.BuildConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.ExternalAuthAction
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClientManager {
    private var _supabaseClient: SupabaseClient? = null

    // Initialize SupabaseClient if it hasn't been initialized yet
    fun getSupabaseClient(context: Context): SupabaseClient {
        if (_supabaseClient == null) {
            _supabaseClient = createSupabaseClient(
                supabaseUrl = BuildConfig.SUPABASE_URL,
                supabaseKey = BuildConfig.SUPABASE_ANON_KEY
            ) {
                install(Auth) {
                    // Configure the Auth plugin
                    scheme = "gym4every1.app" // Define the custom scheme for your app's deeplink
                    host = "reset_password" // Host could be your app's package name or URL
                    // Optional: Open in a custom tab instead of an external browser
                    defaultExternalAuthAction = ExternalAuthAction.CustomTabs()

                    flowType = io.github.jan.supabase.auth.FlowType.PKCE
                }
                install(Postgrest)
            }
        }
        return _supabaseClient!!
    }
}
