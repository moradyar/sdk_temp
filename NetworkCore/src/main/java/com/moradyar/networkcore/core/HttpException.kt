package com.moradyar.networkcore.core

class HttpException(
    val code: Int,
    message: String
) : Exception(message)