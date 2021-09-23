package com.dtalonso.data.requests


data class CreateWeightRequest(
    val exerciseId: String,
    val weight: Float,
    val comment: String
)
