package com.github.imhenoch.cinema.api.movie.services

import com.github.imhenoch.cinema.api.common.DatabaseFactory.query
import com.github.imhenoch.cinema.api.movie.models.Genre
import com.github.imhenoch.cinema.api.movie.models.Genres
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

class GenreService {
    suspend fun findAll(): List<Genre> = query {
        Genres
            .selectAll()
            .toGenres()
    }

    suspend fun findBy(id: Int): Genre? = query {
        Genres
            .select {
                (Genres.id eq id)
            }
            .toGenres()
            .singleOrNull()
    }

    suspend fun create(genre: Genre): Genre {
        var key = 0
        query {
            key = (Genres.insert {
                it[Genres.genre] = genre.genre
            } get Genres.id)!!
        }
        return findBy(key)!!
    }

    suspend fun save(genre: Genre): Genre? {
        val id = genre.id
        return if (id == null) {
            null
        } else {
            query {
                Genres.update({ Genres.id eq id }) {
                    it[Genres.genre] = genre.genre
                }
            }
            findBy(id)
        }
    }

    suspend fun delete(id: Int): Boolean = query {
        Genres.deleteWhere { Genres.id eq id } > 0
    }
}

fun Iterable<ResultRow>.toGenres() =
    this.map(ResultRow::toGenre)

fun ResultRow.toGenre() = Genre(
    id = this[Genres.id],
    genre = this[Genres.genre]
)