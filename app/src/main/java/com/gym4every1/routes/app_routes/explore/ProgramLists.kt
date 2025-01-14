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

    // Back Exercises Warm-Up
    val backWarmUp = listOf(
        Exercise("Cat_Stretch", 30, 10),
        Exercise("Barbell_Deadlift", 30, 10)
    )

    val chestWarmUp = listOf(
        Exercise("Push-Up_Wide", 30, 10),
        Exercise("Dumbbell_Bench_Press", 30, 10)
    )

    // Core Exercises Warm-Up
    val coreWarmUp = listOf(
        Exercise("Plank", 30, 10),
        Exercise("Crunches", 30, 10)
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
// Back Exercises based on levels
val backExercises = when (level) {
    "Easy" -> listOf(
        Exercise("Bent_Over_Two-Dumbbell_Row", 60, 20),
        Exercise("Seated_Cable_Shoulder_Press", 60, 20),
        Exercise("Reverse_Flyes_With_External_Rotation", 60, 20),
        Exercise("One-Arm_Kettlebell_Row", 60, 20),
        Exercise("Bent_Over_Two-Dumbbell_Row_With_Palms_In", 60, 20)
    )
    "Medium" -> listOf(
        Exercise("Reverse_Grip_Bent-Over_Rows", 120, 30),
        Exercise("Seated_Cable_Shoulder_Press", 120, 30),
        Exercise("Face_Pull", 120, 30),
        Exercise("Leverage_Decline_Chest_Press", 120, 30),
        Exercise("Reverse_Triceps_Bench_Press", 120, 30)
    )
    "Expert" -> listOf(
        Exercise("Deficit_Deadlift", 180, 30),
        Exercise("Flat_Bench_Leg_Pull-In", 180, 30),
        Exercise("Double_Kettlebell_Alternating_Hang_Clean", 180, 30),
        Exercise("Push-Ups_With_Feet_Elevated", 180, 30),
        Exercise("Scissor_Kick", 180, 30)
    )
    else -> emptyList()
}

val chestExercises = when (level) {
    "Easy" -> listOf(
        Exercise("Flat_Bench_Cable_Flyes", 60, 20),
        Exercise("Dumbbell_Flyes", 60, 20),
        Exercise("Machine_Bench_Press", 60, 20),
        Exercise("Double_Kettlebell_Push_Press", 60, 20),
        Exercise("Cable_Crossover", 60, 20)
    )
    "Medium" -> listOf(
        Exercise("Incline_Hammer_Curls", 120, 30),
        Exercise("Front_Cable_Raise", 120, 30),
        Exercise("Cable_Chest_Press", 120, 30),
        Exercise("Dumbbell_Shoulder_Press", 120, 30),
        Exercise("Clean_Pull", 120, 30)
    )
    "Expert" -> listOf(
        Exercise("Smith_Machine_Incline_Bench_Press", 180, 30),
        Exercise("Wide-Grip_Barbell_Bench_Press", 180, 30),
        Exercise("Band_Good_Morning", 180, 30),
        Exercise("Sledgehammer_Swings", 180, 30),
        Exercise("Reverse_Band_Bench_Press", 180, 30)
    )
    else -> emptyList()
}

// Core Exercises based on levels
val coreExercises = when (level) {
    "Easy" -> listOf(
        Exercise("Sit-Up", 60, 20),
        Exercise("Flutter_Kicks", 60, 20),
        Exercise("Cross_Over_-_With_Bands", 60, 20),
        Exercise("Parallel_Bar_Dip", 60, 20),
        Exercise("Lying_Close-Grip_Barbell_Triceps_Extension_Behind_The_Head", 60, 20)
    )
    "Medium" -> listOf(
        Exercise("Platform_Hamstring_Slides", 120, 30),
        Exercise("Sandbag_Load", 120, 30),
        Exercise("Floor_Press_with_Chains", 120, 30),
        Exercise("Shoulder_Press_-_With_Bands", 120, 30),
        Exercise("Flat_Bench_Cable_Flyes", 120, 30)
    )
    "Expert" -> listOf(
        Exercise("Hang_Snatch_-_Below_Knees", 180, 30),
        Exercise("Hanging_Bar_Good_Morning", 180, 30),
        Exercise("One-Arm_Kettlebell_Clean_and_Jerk", 180, 30),
        Exercise("Pushups_Close_and_Wide_Hand_Positions", 180, 30),
        Exercise("Oblique_Crunches_-_On_The_Floor", 180, 30)
    )
    else -> emptyList()
}

// Combine all exercises into a single list for the workout program
val program = when (programName) {
    "Cardio" -> cardioWarmUp + cardioExercises + generalCoolDown
    "Arm Training" -> armWarmUp + armExercises + generalCoolDown
    "Lower Body Training" -> lowerBodyWarmUp + lowerBodyExercises + generalCoolDown
    "Back Training" -> backWarmUp + backExercises + generalCoolDown
    "Chest Training" -> chestWarmUp + chestExercises + generalCoolDown
    "Core Training" -> coreWarmUp + coreExercises + generalCoolDown
    else -> emptyList()
}

return program
}