package com.dtalonso.data.repository.user

import com.dtalonso.data.models.Weight
import com.dtalonso.data.util.MuscularGroup
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class WeightsRepositoryImpl(
    db: CoroutineDatabase
): WeightsRepository {

    private val weights = db.getCollection<Weight>()

    override suspend fun createWeight(weight: Weight) {
        weights.insertOne(weight)
    }

    override suspend fun getWeights(userId: String, exerciseId: String): List<Weight> {
        return weights.find(
            and(
                Weight::userId eq userId,
                Weight::exerciseId eq exerciseId
            )
        ).toList()
    }

    override suspend fun getWeightById(weightId: String): Weight? {
        return weights.findOneById(weightId)
    }


    override suspend fun updateWeight(weight: Weight) {
        weights.updateOneById(weight.id, weight)
    }

    override suspend fun deleteWeight(weightId: String) {
        weights.deleteOneById(weightId)
    }
}