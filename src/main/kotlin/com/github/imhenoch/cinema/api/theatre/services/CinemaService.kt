package com.github.imhenoch.cinema.api.theatre.services

import com.github.imhenoch.cinema.api.common.DatabaseFactory.query
import com.github.imhenoch.cinema.api.theatre.models.Cinema
import com.github.imhenoch.cinema.api.theatre.models.Cinemas
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

class CinemaService {
    suspend fun findAll(): List<Cinema> = query {
        Cinemas
            .selectAll()
            .toCinemas()
    }

    suspend fun findBy(id: Int): Cinema? = query {
        Cinemas
            .select {
                (Cinemas.id eq id)
            }
            .toCinemas()
            .singleOrNull()
    }

    suspend fun create(cinema: Cinema): Cinema {
        var key = 0
        query {
            key = (Cinemas.insert {
                it[Cinemas.name] = cinema.name
                it[Cinemas.row] = cinema.row
                it[Cinemas.column] = cinema.column
            } get Cinemas.id)!!
        }
        return findBy(key)!!
    }

    suspend fun save(cinema: Cinema): Cinema? {
        val id = cinema.id
        return if (id == null) {
            null
        } else {
            query {
                Cinemas.update({ Cinemas.id eq id }) {
                    it[Cinemas.name] = cinema.name
                    it[Cinemas.row] = cinema.row
                    it[Cinemas.column] = cinema.column
                }
            }
            findBy(id)
        }
    }

    suspend fun delete(id: Int): Boolean = query {
        Cinemas.deleteWhere { Cinemas.id eq id } > 0
    }
}

fun Iterable<ResultRow>.toCinemas() =
    this.map(ResultRow::toCinema)

fun ResultRow.toCinema() = Cinema(
    id = this[Cinemas.id],
    name = this[Cinemas.name],
    row = this[Cinemas.row],
    column = this[Cinemas.column]
)