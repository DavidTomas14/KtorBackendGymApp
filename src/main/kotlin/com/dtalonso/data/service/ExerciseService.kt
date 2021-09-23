package com.dtalonso.data.service

import com.dtalonso.data.models.Exercise
import com.dtalonso.data.repository.user.ExerciseRepository
import com.dtalonso.data.requests.CreateExerciseRequest
import com.dtalonso.data.requests.UpdateExerciseRequest
import com.dtalonso.data.util.IncreasinWeightValoration
import com.dtalonso.data.util.MuscularGroup

class ExerciseService(
    private val exerciseRepository: ExerciseRepository
){

    suspend fun getExerciseById(exerciseId: String): Exercise? {
        return exerciseRepository.getExerciseById(exerciseId)
    }

    suspend fun getExercisesByMuscularGroup(muscularGroup: MuscularGroup, userId: String): List<Exercise> {
        return exerciseRepository.getExercisesByMuscularGroup(muscularGroup, userId)
    }

    suspend fun addExerciseInMuscularGroup(exercise: CreateExerciseRequest, userId: String): ValidationEvent {
        if (exercise.description.isBlank() || exercise.name.isBlank()) {
            return ValidationEvent.ErrorFieldEmpty
        }else if (exercise.muscularGroup !in enumValues<MuscularGroup>()) {
            return ValidationEvent.ErrorMuscularGroup
        }
        else{
            exerciseRepository.createExerciseInMuscularGroup(
                Exercise(
                    userId = userId,
                    name = exercise.name,
                    description = exercise.description,
                    muscularGroup = exercise.muscularGroup,
                    "",
                    IncreasinWeightValoration.Sad
                )
            )
            return ValidationEvent.Success
        }

    }

    suspend fun updateExercise(request: UpdateExerciseRequest): ValidationEvent {
        if (request.exerciseId.isBlank() || request.description.isBlank() ||
            request.increaseWeightComment.isBlank()) {
            return ValidationEvent.ErrorFieldEmpty

        }else if (request.increasingWeightValoration !in enumValues<IncreasinWeightValoration>()) {
            return ValidationEvent.ErrorMuscularGroup
        }
        else{
            val oldExercise = exerciseRepository.getExerciseById(request.exerciseId)
            val newExercise = oldExercise?.copy(
                description = request.description,
                inscreaseWeightComment = request.increaseWeightComment,
                increasinWeightValoration = request.increasingWeightValoration
            ) ?: return ValidationEvent.ErrorNullExercise
            exerciseRepository.updatExercise(newExercise)
            return ValidationEvent.Success
        }
    }

    suspend fun deleteExercise(parentId: String) {
        exerciseRepository.deleteExercise(parentId)
    }

    sealed class ValidationEvent() {
        object ErrorFieldEmpty: ValidationEvent()
        object ErrorMuscularGroup: ValidationEvent()
        object ErrorNullExercise: ValidationEvent()
        object Success: ValidationEvent()
    }
}