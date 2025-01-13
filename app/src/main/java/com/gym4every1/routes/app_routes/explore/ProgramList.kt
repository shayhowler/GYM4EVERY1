package com.gym4every1.routes.app_routes.explore

import com.gym4every1.models.workout_plans_models.Exercise

// Updated fetchWorkoutProgram function with all exercises categories integrated
fun fetchWorkoutProgram(programName: String, level: String = "Easy"): List<Exercise> {
    // General Warm-Up Exercises (Cardio)
    val cardioWarmUp = listOf(
        Exercise("Rope_Jumping", 30, 10),
        Exercise("Air_Bike", 30, 10)
    )

    // General Cool-Down & Stretch Exercises (Cardio)
    val generalCoolDown = listOf(
        Exercise("Cat_Stretch", 30, 0),
        Exercise("Quad_Stretch", 30, 0),
        Exercise("Iliotibial_Tract-SMR", 30, 0)
    )

    // Arm Exercises Warm-Up
    val armWarmUp = listOf(
        Exercise("Arm_Circles", 30, 10),
        Exercise("Alternate_Hammer_Curl", 30, 10)
    )

    // Lower Body Exercises Warm-Up
    val lowerBodyWarmUp = listOf(
        Exercise("Bodyweight_Squat", 30, 10),
        Exercise("Hip_Circles_prone", 30, 10)
    )

    // Cardio Exercises
    val cardioExercises = listOf(
        Exercise("Running_Treadmill", 600, 30),
        Exercise("Bicycling_Stationary", 600, 30),
        Exercise("Rope_Jumping", 120, 30),
        Exercise("Rowing_Stationary", 600, 30),
        Exercise("Stairmaster", 600, 30),
        Exercise("Elliptical_Trainer", 600, 30),
        Exercise("Mountain_Climbers", 30, 20),
        Exercise("Sled_Push", 60, 30),
        Exercise("Wind_Sprints", 30, 20),
        Exercise("Box_Jump_Multiple_Response", 30, 30)
    )

    // Arm Exercises based on levels
    val armExercises = when (level) {
        "Easy" -> listOf(
            Exercise("Dumbbell_Alternate_Bicep_Curl", 60, 20),
            Exercise("EZ-Bar_Curl", 60, 20),
            Exercise("Hammer_Curls", 60, 20),
            Exercise("Cross_Body_Hammer_Curl", 60, 20),
            Exercise("Concentration_Curls", 60, 20)
        )
        "Medium" -> listOf(
            Exercise("Close-Grip_Dumbbell_Press", 120, 30),
            Exercise("Dumbbell_Shrug", 120, 30),
            Exercise("Pullups", 120, 30),
            Exercise("Standing_Biceps_Stretch", 120, 30),
            Exercise("Side_Wrist_Pull", 120, 30)
        )
        "Expert" -> listOf(
            Exercise("Zottman_Curl", 180, 30),
            Exercise("Anterior_Tibialis-SMR", 180, 30),
            Exercise("Arnold_Dumbbell_Press", 180, 30),
            Exercise("Backward_Drag", 180, 30),
            Exercise("All_Fours_Quad_Stretch", 180, 30)
        )
        else -> emptyList()
    }

    // Lower Body Exercises based on levels
    val lowerBodyExercises = when (level) {
        "Easy" -> listOf(
            Exercise("Dumbbell_Squat", 60, 20),
            Exercise("Goblet_Squat", 60, 20),
            Exercise("Single_Leg_Butt_Kick", 60, 20),
            Exercise("Leg_Lift", 60, 20),
            Exercise("Dumbbell_Lunges", 60, 20)
        )
        "Medium" -> listOf(
            Exercise("Barbell_Side_Split_Squat", 120, 30),
            Exercise("Glute_Ham_Raise", 120, 30),
            Exercise("Standing_Dumbbell_Reverse_Curl", 120, 30),
            Exercise("Single-Arm_Cable_Crossover", 120, 30),
            Exercise("Smith_Machine_Bench_Press", 120, 30)
        )
        "Expert" -> listOf(
            Exercise("Sumo_Deadlift_with_Bands", 180, 30),
            Exercise("Kettlebell_Figure_8", 180, 30),
            Exercise("Front_Dumbbell_Raise", 180, 30),
            Exercise("Standing_Pelvic_Tilt", 180, 30),
            Exercise("Gironda_Sternum_Chins", 180, 30)
        )
        else -> emptyList()
    }

    // Combine all exercises into a single list for the workout program
    val program = when (programName) {
        "Cardio" -> cardioWarmUp + cardioExercises + generalCoolDown
        "Hand Training" -> armWarmUp + armExercises + generalCoolDown
        "Lower Body Training" -> lowerBodyWarmUp + lowerBodyExercises + generalCoolDown
        else -> emptyList()
    }

    return program
}