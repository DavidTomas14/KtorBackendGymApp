package com.dtalonso.data.requests

import com.dtalonso.data.util.IncreasinWeightValoration
import com.dtalonso.data.util.MuscularGroup
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class UpdateExerciseRequest(
    val exerciseId: String,
    val description: String,
    val increaseWeightComment: String,
    val increasingWeightValoration: IncreasinWeightValoration,
)
