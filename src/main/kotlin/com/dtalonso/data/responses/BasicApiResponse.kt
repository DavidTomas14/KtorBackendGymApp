package com.dtalonso.data.responses

class BasicApiResponse<T>(
    val successful: Boolean,
    val message: String? = null,
    val data: T? = null
) {
}