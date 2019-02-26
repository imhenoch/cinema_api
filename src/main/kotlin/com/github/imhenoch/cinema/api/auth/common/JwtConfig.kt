package com.github.imhenoch.cinema.api.auth.common

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.github.imhenoch.cinema.api.auth.models.User
import java.util.Date

object JwtConfig {
    private const val secret = "zAP5MBA4B4Ijz0MZaS48"
    private const val issuer = "ktor.io"
    private const val validityInMs = 3_600 * 10 * 1000
    private val algorithm = Algorithm.HMAC512(secret)

    fun generateToken(user: User) =
        JWT
            .create()
            .withSubject("Authentication")
            .withIssuer(issuer)
            .withClaim("id", user.id)
            .withExpiresAt(getExpiration())
            .sign(algorithm)

    private fun getExpiration() = Date(System.currentTimeMillis() + validityInMs)

    data class LoggedUser(
        val id: Int,
        val email: String,
        val token: String
    )
}