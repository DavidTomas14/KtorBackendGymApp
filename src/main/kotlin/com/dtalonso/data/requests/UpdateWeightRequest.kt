package com.dtalonso.data.requests

data class UpdateWeightRequest(
    val weightId: String,
    val weight: Float,
    val comment: String,
)
