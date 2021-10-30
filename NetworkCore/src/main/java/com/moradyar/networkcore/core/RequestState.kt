package com.moradyar.networkcore.core

sealed class RequestState {

    class Success(val value: String) : RequestState()

    class Error(val e: Exception) : RequestState()
}
