package com.dtalonso.data.repository.user

import com.dtalonso.data.models.Weight
import com.dtalonso.data.util.MuscularGroup

interface WeightsRepository {

    suspend fun createWeight(weight: Weight)

    suspend fun getWeights(userId: String, exerciseId: String) : List<Weight>

    suspend fun getWeightById(weightId:String): Weight?

    suspend fun updateWeight(weight: Weight)

    suspend fun deleteWeight(weightId: String)

}