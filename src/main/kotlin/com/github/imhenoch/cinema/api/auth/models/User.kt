package com.github.imhenoch.cinema.api.auth.models

import org.jetbrains.exposed.sql.Table

object Users : Table() {
    val id = integer("id").primaryKey(0).autoIncrement()
    val email = varchar("email", 100).primaryKey(1)
    val hash = varchar("hash", 60)
}

data class User(
    val id: Int?,
    val email: String,
    val password: String
)