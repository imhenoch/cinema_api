package com.github.imhenoch.cinema.api.theatre.models

import com.github.imhenoch.cinema.api.movie.models.Film
import com.github.imhenoch.cinema.api.movie.models.Films
import org.jetbrains.exposed.sql.Table

object Functions : Table() {
    val id = integer("id").primaryKey().autoIncrement()
    val film = reference("film", Films.id)
    val cinema = reference("cinema", Cinemas.id)
    val date = varchar("date", length = 50)
}

data class Function(
    val id: Int?,
    val film: Film?,
    val cinema: Cinema?,
    val date: String
)