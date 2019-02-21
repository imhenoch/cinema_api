package com.github.imhenoch.cinema.api.movie.models

import org.jetbrains.exposed.sql.Table

object Ratings : Table() {
    val id = integer("id").primaryKey().autoIncrement()
    val rating = varchar("rating", 10)
}

data class Rating(
    val id: Int?,
    val rating: String
)