package com.github.imhenoch.cinema.api.movie.services

import com.github.imhenoch.cinema.api.common.DatabaseFactory.query
import com.github.imhenoch.cinema.api.movie.models.Film
import com.github.imhenoch.cinema.api.movie.models.Films
import com.github.imhenoch.cinema.api.movie.models.Ratings
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class FilmService {
    private val FullFilm =
        (Films innerJoin Ratings)

    suspend fun findAll() = query {
        FullFilm
            .selectAll()
            .toFilms()
    }

    suspend fun findBy(id: Int) = query {
        FullFilm
            .select {
                Films.id eq id
            }
            .toFilms()
            .singleOrNull()
    }
}

fun Iterable<ResultRow>.toFilms() =
    map(ResultRow::toFilm)

fun ResultRow.toFilm() = Film(
    id = this[Films.id],
    name = this[Films.name],
    duration = this[Films.duration],
    rating = toRating(),
    genres = emptyList(),
    languages = emptyList()
)