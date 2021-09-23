package com.dtalonso.data.service

import com.dtalonso.data.models.Weight
import com.dtalonso.data.repository.user.WeightsRepository
import com.dtalonso.data.requests.CreateWeightRequest
import com.dtalonso.data.requests.UpdateWeightRequest

class WeightsService(
    private val weightsRepository: WeightsRepository
) {
    suspend fun createWeight(weightRequest: CreateWeightRequest, userId: String): ValidationEvent {
        if(weightRequest.comment.isBlank() || weightRequest.exerciseId.isBlank()){
            return ValidationEvent.ErrorFieldEmpty
        }else if (weightRequest.weight.isNaN()){
            return ValidationEvent.ErrorWeightIsNaN
        }else {
            weightsRepository.createWeight(
                Weight(
                    exerciseId = weightRequest.exerciseId,
                    userId = userId,
                    weight = weightRequest.weight,
                    comment = weightRequest.comment,
                    date = System.currentTimeMillis()
                )
            )
            return ValidationEvent.Success
        }
    }

    suspend fun getWeights(exerciseId: String, userId: String): List<Weight> {
        return weightsRepository.getWeights(userId, exerciseId)
    }
    suspend fun getWeightById(weightId: String): Weight? {
        return weightsRepository.getWeightById(weightId)
    }
    suspend fun updateWeight(request: UpdateWeightRequest): ValidationEvent {
        if(request.comment.isBlank() || request.weightId.isBlank()){
            return ValidationEvent.ErrorFieldEmpty
        }else if (request.weight.isNaN()){
            return ValidationEvent.ErrorWeightIsNaN
        }else {
            val oldWeight = weightsRepository.getWeightById(request.weightId)
            val newWeight = oldWeight?.copy(
                comment = request.comment,
                weight = request.weight
            )?: return ValidationEvent.ErrorNullWeight
            weightsRepository.updateWeight(newWeight)
            return ValidationEvent.Success
        }
    }

    suspend fun deleteWeight(weightId: String) {
        weightsRepository.deleteWeight(weightId)
    }

    sealed class ValidationEvent() {
        object ErrorFieldEmpty : ValidationEvent()
        object ErrorWeightIsNaN : ValidationEvent()
        object ErrorNullWeight : ValidationEvent()
        object Success : ValidationEvent()
    }
}

