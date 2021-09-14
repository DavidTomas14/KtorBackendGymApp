package com.dtalonso.data.models

import com.dtalonso.data.util.MuscularGroup
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Weight(
    @BsonId
    val id: String = ObjectId().toString(),
    val exerciseId: String,
    val userId: String,
    val muscularGroup: MuscularGroup,
    val comment: String,
    val date: Long
)
