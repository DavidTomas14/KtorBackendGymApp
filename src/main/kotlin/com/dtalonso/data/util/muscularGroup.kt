package com.dtalonso.data.util

enum class MuscularGroup {
    Biceps,
    Back,
    Triceps,
    Chest,
    Shoulders,
    Legs,
    Abdominales,
    Measurements
}
fun String.toMuscularGroup(): MuscularGroup? {
    when(this){
        "Biceps" -> return MuscularGroup.Biceps
        "Back" -> return MuscularGroup.Back
        "Triceps" -> return MuscularGroup.Triceps
        "Chest" -> return MuscularGroup.Chest
        "Shoulders" -> return MuscularGroup.Shoulders
        "Legs" -> return MuscularGroup.Legs
        "Measurements" -> return MuscularGroup.Measurements
        else-> return null
    }
}