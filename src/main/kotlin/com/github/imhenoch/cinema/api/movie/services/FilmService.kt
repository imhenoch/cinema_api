package com.github.imhenoch.cinema.api.movie.services

import com.github.imhenoch.cinema.api.common.DatabaseFactory.query
import com.github.imhenoch.cinema.api.movie.models.Film
import com.github.imhenoch.cinema.api.movie.models.FilmGenres
import com.github.imhenoch.cinema.api.movie.models.FilmLanguages
import com.github.imhenoch.cinema.api.movie.models.Films
import com.github.imhenoch.cinema.api.movie.models.Genres
import com.github.imhenoch.cinema.api.movie.models.Languages
import com.github.imhenoch.cinema.api.movie.models.Rating
import com.github.imhenoch.cinema.api.movie.models.Ratings
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.update

class FilmService {
    private val FullFilm =
        (Films innerJoin Ratings innerJoin FilmGenres innerJoin Genres innerJoin FilmLanguages innerJoin Languages)

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

    suspend fun create(film: Film) = query {
        val savedFilm = (Films.insert { it.from(film) }[Films.id])
            .let { film.copy(id = it) }
        savedFilm.genres.forEach { genre ->
            FilmGenres.insert {
                it[FilmGenres.filmId] = savedFilm.id!!
                it[FilmGenres.genreId] = genre.id!!
            }
        }
        savedFilm.languages.forEach { language ->
            FilmLanguages.insert {
                it[FilmLanguages.filmId] = savedFilm.id!!
                it[FilmLanguages.languageId] = language.id!!
            }
        }
        savedFilm
    }

    suspend fun save(film: Film) =
        if (film.id == null) {
            null
        } else {
            query {
                Films.update({ Films.id eq film.id }) { it.from(film) }
            }
            film
        }

    suspend fun delete(id: Int): Boolean = query {
        FilmLanguages.deleteWhere { FilmLanguages.filmId eq id }
        FilmGenres.deleteWhere { FilmGenres.filmId eq id }
        Films.deleteWhere { Films.id eq id } > 0
    }
}

fun Iterable<ResultRow>.toFilms() =
    fold(mutableMapOf<Int, Film>()) { map, resultRow ->
        val film = resultRow.toFilm()
        val languageId = resultRow.tryGet(Languages.id)
        val language = languageId?.let { resultRow.toLanguage() }
        val genreId = resultRow.tryGet(Genres.id)
        val genre = genreId?.let { resultRow.toGenre() }
        val current = map.getOrDefault(film.id, film)
        map[film.id!!] = current.copy(
            genres = (current.genres + listOfNotNull(genre)).distinct(),
            languages = (current.languages + listOfNotNull(language)).distinct()
        )
        map
    }.values.toList()

fun ResultRow.toFilm() = Film(
    id = this[Films.id],
    name = this[Films.name],
    duration = this[Films.duration],
    rating = toRating(),
    genres = emptyList(),
    languages = emptyList()
)

fun UpdateBuilder<Any>.from(film: Film) = this.run {
    this[Films.name] = film.name
    this[Films.duration] = film.duration
    this[Films.rating] = film.rating.id!!
}