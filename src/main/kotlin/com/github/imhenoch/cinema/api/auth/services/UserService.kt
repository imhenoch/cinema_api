package com.github.imhenoch.cinema.api.auth.services

import com.github.imhenoch.cinema.api.auth.models.User
import com.github.imhenoch.cinema.api.auth.models.Users
import com.github.imhenoch.cinema.api.common.DatabaseFactory.query
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.mindrot.jbcrypt.BCrypt

class UserService {
    suspend fun create(user: User) = query {
        (Users.insert { it.from(user) }[Users.id])
            .let { user.copy(id = it, password = "") }
    }
}

fun UpdateBuilder<Any>.from(user: User) = this.run {
    this[Users.email] = user.email
    this[Users.hash] = BCrypt.hashpw(user.password, BCrypt.gensalt(12))
}