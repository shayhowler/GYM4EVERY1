package com.gym4every1.routes.shared

import android.content.Context
import android.content.Intent
import com.gym4every1.routes.auth_routes.*
import com.gym4every1.routes.start_routes.*
import com.gym4every1.routes.feed.*
import android.app.Activity
import com.gym4every1.singletons.ProfileViewModel
import com.gym4every1.singletons.SignUpViewModel

class Routes {

    companion object {

        // Navigate to the AuthHomeActivity
        fun navigateToAuthHome(context: Context) {
            ProfileViewModel.clear()
            SignUpViewModel.clear()
            val intent = Intent(context, AuthHomeActivity::class.java)
            (context as? Activity)?.finish()  // Finish the current activity
            context.startActivity(intent)
        }

        // Navigate to the SignInActivity
        fun navigateToSignIn(context: Context) {
            ProfileViewModel.clear()
            SignUpViewModel.clear()
            val intent = Intent(context, SignInActivity::class.java)
            (context as? Activity)?.finish()  // Finish the current activity
            context.startActivity(intent)
        }

        // Navigate to the SignUp1Activity
        fun navigateToSignUp1(context: Context) {
            val intent = Intent(context, SignUp1Activity::class.java)
            (context as? Activity)?.finish()  // Finish the current activity
            context.startActivity(intent)
        }

        // Navigate to the SignUp2Activity
        fun navigateToSignUp2(context: Context) {
            val intent = Intent(context, SignUp2Activity::class.java)
            (context as? Activity)?.finish()  // Finish the current activity
            context.startActivity(intent)
        }

        // Navigate to the ForgotPasswordActivity
        fun navigateToForgotPassword(context: Context) {
            val intent = Intent(context, ForgotPasswordActivity::class.java)
            (context as? Activity)?.finish()  // Finish the current activity
            context.startActivity(intent)
        }

        // Navigate to the GetStartedActivity
        fun navigateToGetStarted(context: Context) {
            val intent = Intent(context, GetStartedActivity::class.java)
            (context as? Activity)?.finish()  // Finish the current activity
            context.startActivity(intent)
        }

        // Navigate to the WeightPageActivity
        fun navigateToWeightPage(context: Context) {
            val intent = Intent(context, WeightPageActivity::class.java)
            (context as? Activity)?.finish()  // Finish the current activity
            context.startActivity(intent)
        }

        // Navigate to the HeightPageActivity
        fun navigateToHeightPage(context: Context) {
            val intent = Intent(context, HeightPageActivity::class.java)
            (context as? Activity)?.finish()  // Finish the current activity
            context.startActivity(intent)
        }

        // Navigate to the BirthdayPageActivity
        fun navigateToBirthdayPage(context: Context) {
            val intent = Intent(context, BirthdayPageActivity::class.java)
            (context as? Activity)?.finish()  // Finish the current activity
            context.startActivity(intent)
        }

        // Navigate to the ActivityPageActivity
        fun navigateToActivityPage(context: Context) {
            val intent = Intent(context, ActivityPageActivity::class.java)
            (context as? Activity)?.finish()  // Finish the current activity
            context.startActivity(intent)
        }

        // Navigate to the GoalPageActivity
        fun navigateToGoalPage(context: Context) {
            val intent = Intent(context, GoalPageActivity::class.java)
            (context as? Activity)?.finish()  // Finish the current activity
            context.startActivity(intent)
        }

        // Navigate to the SuccessPageActivity
        fun navigateToSuccessPage(context: Context) {
            val intent = Intent(context, SuccessPageActivity::class.java)
            (context as? Activity)?.finish()  // Finish the current activity
            context.startActivity(intent)
        }

        // Navigate to the FeedPageActivity
        fun navigateToFeedPage(context: Context) {
            ProfileViewModel.clear()
            SignUpViewModel.clear()
            val intent = Intent(context, FeedPageActivity::class.java)
            (context as? Activity)?.finish()  // Finish the current activity
            context.startActivity(intent)
        }
    }
}