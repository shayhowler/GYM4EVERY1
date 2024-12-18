package com.gym4every1

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.gym4every1.routes.auth_routes.AuthHomeActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Launch the first activity (AuthHomeActivity in this case)
        val intent = Intent(this, AuthHomeActivity::class.java)
        startActivity(intent)

        // Finish MainActivity so it doesn't remain in the back stack
        finish()
    }
}