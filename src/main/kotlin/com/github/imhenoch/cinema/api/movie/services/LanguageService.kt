package com.github.imhenoch.cinema.api.movie.services

import com.github.imhenoch.cinema.api.common.DatabaseFactory.query
import com.github.imhenoch.cinema.api.movie.models.Language
import com.github.imhenoch.cinema.api.movie.models.Languages
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

class LanguageService {
    suspend fun findAll(): List<Language> = query {
        Languages
            .selectAll()
            .toLanguages()
    }

    suspend fun findBy(id: Int): Language? = query {
        Languages
            .select {
                (Languages.id eq id)
            }
            .toLanguages()
            .singleOrNull()
    }

    suspend fun create(language: Language): Language {
        var key = 0
        query {
            key = (Languages.insert {
                it[Languages.language] = language.language
            } get Languages.id)!!
        }
        return findBy(key)!!
    }

    suspend fun save(language: Language): Language? {
        val id = language.id
        return if (id == null) {
            null
        } else {
            query {
                Languages.update({ Languages.id eq id }) {
                    it[Languages.language] = language.language
                }
            }
            findBy(id)
        }
    }

    suspend fun delete(id: Int): Boolean = query {
        Languages.deleteWhere { Languages.id eq id } > 0
    }
}

fun Iterable<ResultRow>.toLanguages() =
    this.map(ResultRow::toLanguage)

fun ResultRow.toLanguage() = Language(
    id = this[Languages.id],
    language = this[Languages.language]
)