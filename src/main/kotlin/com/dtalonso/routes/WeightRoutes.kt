package com.dtalonso.routes

import com.dtalonso.data.requests.CreateWeightRequest
import com.dtalonso.data.requests.DeleteRequest
import com.dtalonso.data.requests.UpdateExerciseRequest
import com.dtalonso.data.requests.UpdateWeightRequest
import com.dtalonso.data.responses.BasicApiResponse
import com.dtalonso.data.service.ExerciseService
import com.dtalonso.data.service.WeightsService
import com.dtalonso.data.service.WeightsService.ValidationEvent.*
import com.dtalonso.data.util.toMuscularGroup
import com.dtalonso.plugins.userId
import com.dtalonso.util.ApiResponseMessages
import com.dtalonso.util.ApiResponseMessages.EXERCISE_DOENT_EXIST
import com.dtalonso.util.ApiResponseMessages.WEIGHT_IS_NAN
import com.dtalonso.util.ApiResponseMessages.WEIGHT_IS_NULL
import com.dtalonso.util.QueryParams
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.createWeight(weightsService: WeightsService) {
    authenticate {
        post("api/weight/create") {
            val request = call.receiveOrNull<CreateWeightRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val user = call.userId
            when(weightsService.createWeight(request,user)){
                is ErrorFieldEmpty -> {
                   call.respond(HttpStatusCode.BadRequest)
                   return@post
                }
                is ErrorWeightIsNaN -> {
                    call.respond(
                        BasicApiResponse(
                            successful = false,
                            message = WEIGHT_IS_NAN
                        )
                    )
                }
                is Success -> {
                    call.respond(
                        BasicApiResponse(
                            successful = true
                        )
                    )
                }
            }
        }
    }
}
fun Route.getWeightsByExercise(weightsService: WeightsService) {
    authenticate {
        get("api/weight/get") {
            val exerciseId = call.parameters[QueryParams.PARAM_EXERCISE_ID] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val user = call.userId
            val weights = weightsService.getWeights(
                userId = user,
                exerciseId = exerciseId
            )
            call.respond(
                status = HttpStatusCode.OK,
                message = weights
            )
        }
    }
}


fun Route.updateWeight(weightsService: WeightsService) {
    authenticate {
        post("api/weight/update") {
            val request = call.receiveOrNull<UpdateWeightRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val user = call.userId
            val weight = weightsService.getWeightById(request.weightId)
            if (weight?.userId != user) {
                call.respond(HttpStatusCode.Unauthorized)
                return@post
            }else{
                when(weightsService.updateWeight(request)){
                    is ErrorFieldEmpty -> {
                        call.respond(
                            BasicApiResponse(
                                successful = false,
                                message = ApiResponseMessages.FIELDS_BLANK
                            )
                        )
                    }
                    is ErrorWeightIsNaN -> {
                        call.respond(
                            BasicApiResponse(
                                successful = false,
                                message = WEIGHT_IS_NAN
                            )
                        )
                    }
                    is ErrorNullWeight -> {
                        call.respond(
                            BasicApiResponse(
                                successful = false,
                                message = WEIGHT_IS_NULL
                            )
                        )
                    }
                    is Success -> {
                        call.respond(
                            BasicApiResponse(
                                successful = true
                            )
                        )
                    }
                }
            }

        }
    }
}

fun Route.deleteWeight(weightsService: WeightsService){
    authenticate {
        delete("api/weight/delete"){
            val request = call.receiveOrNull<DeleteRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }
            val user = call.userId
            val weight = weightsService.getWeightById(request.parentId) ?: kotlin.run {
                call.respond(
                    BasicApiResponse(
                        successful = false,
                        message = EXERCISE_DOENT_EXIST
                    )
                )
                return@delete
            }
            if (weight.userId != user) {
                call.respond(HttpStatusCode.Unauthorized)
                return@delete
            }else{
                weightsService.deleteWeight(request.parentId)
                call.respond(
                    BasicApiResponse(
                        successful = true
                    )
                )
            }
        }
    }
}