package com.dtalonso.plugins


import com.dtalonso.data.service.ExerciseService
import com.dtalonso.data.service.UserService
import com.dtalonso.data.service.WeightsService
import com.dtalonso.routes.*
import io.ktor.routing.*
import io.ktor.application.*
import org.koin.ktor.ext.inject


fun Application.configureRouting() {

    val userService: UserService by inject()
    val exerciseService: ExerciseService by inject()
    val weightsService: WeightsService by inject()

    val jwtIssuer = environment.config.property("jwt.domain").getString()
    val jwtAudience = environment.config.property("jwt.audience").getString()
    val jwtSecret = environment.config.property("jwt.secret").getString()

    routing {

        //User Routes
        authenticate()
        createUser(userService)
        loginUser(
            userService = userService,
            jwtIssuer = jwtIssuer,
            jwtAudience = jwtAudience,
            jwtSecret = jwtSecret
        )

        //Exercise Routes
        createExercise(exerciseService)
        getExercises(exerciseService)
        getExerciseById(exerciseService)
        updateExercise(exerciseService)
        deleteExercise(exerciseService)

        //Weights Routes
        createWeight(weightsService)
        getWeightsByExercise(weightsService)
        updateWeight(weightsService)
        deleteWeight(weightsService)
    }
}
