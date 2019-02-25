package com.github.imhenoch.cinema.api.common

import com.github.imhenoch.cinema.api.movie.models.FilmGenres
import com.github.imhenoch.cinema.api.movie.models.FilmLanguages
import com.github.imhenoch.cinema.api.movie.models.Films
import com.github.imhenoch.cinema.api.movie.models.Genres
import com.github.imhenoch.cinema.api.movie.models.Languages
import com.github.imhenoch.cinema.api.movie.models.Ratings
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        val config = HikariConfig("/hikari.properties")
        Database.connect(HikariDataSource(config))
        transaction {
            create(Languages)
            create(Genres)
            create(Ratings)
            create(Films)
            create(FilmGenres)
            create(FilmLanguages)
        }
    }

    suspend fun <T> query(block: () -> T): T =
        withContext(Dispatchers.IO) {
            transaction { block() }
        }
}