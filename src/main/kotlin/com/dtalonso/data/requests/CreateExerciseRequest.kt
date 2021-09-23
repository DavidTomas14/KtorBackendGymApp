package com.dtalonso.data.requests

import com.dtalonso.data.util.IncreasinWeightValoration
import com.dtalonso.data.util.MuscularGroup
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class CreateExerciseRequest(
    val name: String,
    val description: String,
    val muscularGroup: MuscularGroup,
)
