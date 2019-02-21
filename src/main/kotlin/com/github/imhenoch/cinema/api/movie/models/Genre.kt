package com.github.imhenoch.cinema.api.movie.models

import org.jetbrains.exposed.sql.Table

object Genres : Table() {
    val id = integer("id").primaryKey().autoIncrement()
    val genre = varchar("genre", 50)
}

data class Genre(
    val id: Int?,
    val genre: String
)