package com.dtalonso.routes

import com.dtalonso.data.requests.CreateExerciseRequest
import com.dtalonso.data.requests.DeleteRequest
import com.dtalonso.data.requests.UpdateExerciseRequest
import com.dtalonso.data.responses.BasicApiResponse
import com.dtalonso.data.service.ExerciseService
import com.dtalonso.data.service.ExerciseService.ValidationEvent.*
import com.dtalonso.data.service.WeightsService
import com.dtalonso.data.util.MuscularGroup
import com.dtalonso.data.util.toMuscularGroup
import com.dtalonso.plugins.userId
import com.dtalonso.util.ApiResponseMessages
import com.dtalonso.util.ApiResponseMessages.EXERCISE_DOENT_EXIST
import com.dtalonso.util.ApiResponseMessages.EXERCISE_IS_NULL
import com.dtalonso.util.ApiResponseMessages.FIELDS_BLANK
import com.dtalonso.util.ApiResponseMessages.MUSCULAR_ERROR_DOESNT_EXIST
import com.dtalonso.util.QueryParams.PARAM_EXERCISE_ID
import com.dtalonso.util.QueryParams.PARAM_MUSCULAR_GROUP
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.get

fun Route.createExercise(exerciseService: ExerciseService) {
    authenticate {
        post("api/exercise/create") {
            val request = call.receiveOrNull<CreateExerciseRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val user = call.userId
            when (exerciseService.addExerciseInMuscularGroup(request, user)) {
                is ErrorFieldEmpty -> {
                    call.respond(
                        BasicApiResponse(
                            successful = false,
                            message = FIELDS_BLANK
                        )
                    )
                }
                is ErrorMuscularGroup -> {
                    call.respond(
                        BasicApiResponse(
                            successful = false,
                            message = MUSCULAR_ERROR_DOESNT_EXIST
                        )
                    )
                }
                is Success -> {
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse(
                            successful = true
                        )

                    )

                }
            }
        }
    }
}

fun Route.getExercises(exerciseService: ExerciseService) {
    authenticate {
        get("api/exercise/getexercises") {

            val muscularGroupString = call.parameters[PARAM_MUSCULAR_GROUP] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            print(muscularGroupString)
            val user = call.userId
            val exercises = exerciseService.getExercisesByMuscularGroup(
                muscularGroup = muscularGroupString.toMuscularGroup()!!,
                userId = user
            )
            call.respond(
                status = HttpStatusCode.OK,
                message = exercises
            )
        }
    }
}

fun Route.getExerciseById(exerciseService: ExerciseService) {
    authenticate {
        get("api/exercise/getexercisebyId") {
            val exerciseId = call.parameters[PARAM_EXERCISE_ID] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val exercise = exerciseService.getExerciseById(exerciseId) ?: ""
            call.respond(
                status = HttpStatusCode.OK,
                message = exercise)
        }
    }
}

fun Route.updateExercise(exerciseService: ExerciseService) {
    authenticate {
        post("/api/exercise/update") {
            val request = call.receiveOrNull<UpdateExerciseRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val user = call.userId
            val exercise = exerciseService.getExerciseById(request.exerciseId) ?: kotlin.run {
                call.respond(HttpStatusCode.NoContent)
                return@post
            }

            if (exercise.userId != user) {
                call.respond(HttpStatusCode.Unauthorized)
                return@post
            }else{
               when(exerciseService.updateExercise(request)){
                   is ErrorFieldEmpty -> {
                       call.respond(
                           BasicApiResponse(
                               successful = false,
                               message = FIELDS_BLANK
                           )
                       )
                   }
                   is ErrorMuscularGroup -> {
                       call.respond(
                           BasicApiResponse(
                               successful = false,
                               message = MUSCULAR_ERROR_DOESNT_EXIST
                           )
                       )
                   }
                   is ErrorNullExercise->{
                       call.respond(
                           BasicApiResponse(
                               successful = false,
                               message = EXERCISE_IS_NULL
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
fun Route.deleteExercise(exerciseService: ExerciseService){
    authenticate {
        delete("/api/exercise/delete"){
            val request = call.receiveOrNull<DeleteRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }
            val user = call.userId
            val weight = exerciseService.getExerciseById(request.parentId) ?: kotlin.run {
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
                exerciseService.deleteExercise(request.parentId)
                call.respond(
                    BasicApiResponse(
                        successful = true
                    )
                )
            }
        }
    }
}