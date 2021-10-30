package com.moradyar.authenticationfeature.core

data class Token(
    val authToken: String,
    val userAgent: String,
    val sessionId: String
) {

    companion object {
        fun getInvalidToken(): Token {
            return Token("", "", "")
        }
    }
}
