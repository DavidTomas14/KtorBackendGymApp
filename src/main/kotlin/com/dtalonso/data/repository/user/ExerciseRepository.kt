package com.dtalonso.data.repository.user

import com.dtalonso.data.models.Exercise
import com.dtalonso.data.util.MuscularGroup

interface ExerciseRepository {

    suspend fun getExerciseById(exerciseId: String) : Exercise?

    suspend fun getExercisesByMuscularGroup(muscularGroup: MuscularGroup, userId: String) : List<Exercise>

    suspend fun createExerciseInMuscularGroup(exercise: Exercise)

    suspend fun updatExercise(exercise: Exercise): Boolean

    suspend fun getExerxiseByNameAndGrupoMuscular(name: String, group: MuscularGroup): Exercise?

    suspend fun deleteExercise(exerciseId: String)

}