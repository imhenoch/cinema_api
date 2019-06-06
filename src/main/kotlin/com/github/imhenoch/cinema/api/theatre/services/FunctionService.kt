package com.github.imhenoch.cinema.api.theatre.services

import com.github.imhenoch.cinema.api.common.DatabaseFactory.query
import com.github.imhenoch.cinema.api.movie.models.Films
import com.github.imhenoch.cinema.api.theatre.models.Cinemas
import com.github.imhenoch.cinema.api.theatre.models.Function
import com.github.imhenoch.cinema.api.theatre.models.Functions
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.update

class FunctionService {
    private val FullFunction =
        (Functions innerJoin Cinemas innerJoin Films)

    suspend fun findAll() = query {
        FullFunction
            .selectAll()
            .toFunctions()
    }

    suspend fun findby(filmId: Int) = query {
        FullFunction
            .select {
                Functions.film eq filmId
            }
            .toFunctions()
    }

    suspend fun create(function: Function) = query {
        val savedFunction = (Functions.insert { it.from(function) }[Functions.id])
            .let { function.copy(id = it) }
        savedFunction
    }

    suspend fun save(function: Function) =
        if (function.id == null) {
            null
        } else {
            query {
                Functions.update({ Functions.id eq function.id }) { it.from(function) }
            }
            function
        }

    suspend fun delete(id: Int) = query {
        Functions.deleteWhere { Functions.id eq id } > 0
    }
}

fun Iterable<ResultRow>.toFunctions() =
    this.map(ResultRow::toFunction)

fun ResultRow.toFunction() = Function(
    id = this[Functions.id],
    film = null,
    cinema = toCinema(),
    date = this[Functions.date]
)

fun UpdateBuilder<Any>.from(function: Function) = this.run {
    this[Functions.film] = function.film?.id!!
    this[Functions.cinema] = function.cinema?.id!!
    this[Functions.date] = function.date
}