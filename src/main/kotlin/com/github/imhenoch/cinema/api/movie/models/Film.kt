package com.github.imhenoch.cinema.api.movie.models

import org.jetbrains.exposed.sql.Table

object Films : Table() {
    val id = integer("id").primaryKey().autoIncrement()
    val name = varchar("name", 100)
    val duration = long("duration")
    val rating = reference("rating", Ratings.id)
}

object FilmLanguages : Table("film_languages") {
    val filmId = reference("film_id", Films.id).primaryKey(0)
    val language = reference("language_id", Languages.id).primaryKey(1)
}

object FilmGenres : Table("film_genres") {
    val filmId = reference("film_id", Films.id).primaryKey(0)
    val genreId = reference("genre_id", Genres.id).primaryKey(1)
}

data class Film(
    val id: Int?,
    val name: String,
    val duration: Long,
    val rating: Rating,
    val languages: List<Language>,
    val genres: List<Genre>
)