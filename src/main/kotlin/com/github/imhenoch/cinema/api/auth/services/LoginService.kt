package com.github.imhenoch.cinema.api.auth.services

import com.github.imhenoch.cinema.api.auth.models.Users
import com.github.imhenoch.cinema.api.common.DatabaseFactory.query
import org.jetbrains.exposed.sql.select

class LoginService {
    suspend fun findBy(email: String) = query {
        Users
            .select {
                Users.email eq email
            }
            .toUsers()
            .singleOrNull()
    }
}