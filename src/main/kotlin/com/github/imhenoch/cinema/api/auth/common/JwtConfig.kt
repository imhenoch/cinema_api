package com.github.imhenoch.cinema.api.auth.common

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.github.imhenoch.cinema.api.auth.models.User
import java.util.Date

object JwtConfig {
    private const val secret = "zAP5MBA4B4Ijz0MZaS48"
    private const val issuer = "ktor.io"
    const val audience = "jwt-audience"
    const val realm = "ktor.io"
    private const val validityInMs = 3_600 * 10 * 1000
    private val algorithm = Algorithm.HMAC512(secret)

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()

    fun generateToken(user: User) =
        JWT
            .create()
            .withSubject("Authentication")
            .withIssuer(issuer)
            .withAudience(audience)
            .withClaim("id", user.id)
            .withExpiresAt(getExpiration())
            .sign(algorithm)

    private fun getExpiration() = Date(System.currentTimeMillis() + validityInMs)
}