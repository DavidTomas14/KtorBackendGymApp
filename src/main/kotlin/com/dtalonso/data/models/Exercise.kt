package com.dtalonso.data.models


import com.dtalonso.data.util.IncreasinWeightValoration
import com.dtalonso.data.util.MuscularGroup
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Exercise(
    val userId: String,
    val name: String,
    val description: String,
    val muscularGroup: MuscularGroup,
    val inscreaseWeightComment: String,
    val increasinWeightValoration: IncreasinWeightValoration,
    @BsonId
    val id: String = ObjectId().toString(),
)
