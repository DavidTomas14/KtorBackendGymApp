package com.dtalonso.data.repository.user

import com.dtalonso.data.models.Exercise
import com.dtalonso.data.util.MuscularGroup
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class ExerciseRepositoryImpl(
    db:CoroutineDatabase
): ExerciseRepository{
    private val exercises = db.getCollection<Exercise>()

    override suspend fun getExerciseById(exerciseId: String): Exercise? {
        return exercises.findOneById(exerciseId)
    }

    override suspend fun getExercisesByMuscularGroup(muscularGroup: MuscularGroup, userId: String): List<Exercise> {
        return exercises.find(
            and(Exercise::muscularGroup eq muscularGroup, Exercise::userId eq userId)
        ).toList()
    }

    override suspend fun createExerciseInMuscularGroup(exercise: Exercise){
        exercises.insertOne(exercise)
    }

    override suspend fun updatExercise(exercise: Exercise): Boolean {
        return exercises.updateOneById(exercise.id, exercise).wasAcknowledged()
    }

    override suspend fun getExerxiseByNameAndGrupoMuscular(name: String, group: MuscularGroup): Exercise? {
        return exercises.findOne(
            and(
                Exercise::name eq name,
                Exercise::muscularGroup eq group
            )
        )
    }

    override suspend fun deleteExercise(exerciseId: String) {
        exercises.deleteOneById(exerciseId)
    }
}