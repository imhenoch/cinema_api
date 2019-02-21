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
    suspend fun getAllLanguages(): List<Language> = query {
        Languages
            .selectAll()
            .map(this::toLanguage)
    }

    suspend fun getLanguage(id: Int): Language? = query {
        Languages
            .select {
                (Languages.id eq id)
            }
            .mapNotNull(this::toLanguage)
            .singleOrNull()
    }

    suspend fun addLanguage(language: Language): Language {
        var key = 0
        query {
            key = (Languages.insert {
                it[Languages.language] = language.language
            } get Languages.id)!!
        }
        return getLanguage(key)!!
    }

    suspend fun updateLanguage(language: Language): Language? {
        val id = language.id
        return if (id == null) {
            null
        } else {
            query {
                Languages.update({ Languages.id eq id }) {
                    it[Languages.language] = language.language
                }
            }
            getLanguage(id)
        }
    }

    suspend fun deleteLanguage(id: Int): Boolean = query {
        Languages.deleteWhere { Languages.id eq id } > 0
    }

    private fun toLanguage(row: ResultRow): Language =
        Language(
            id = row[Languages.id],
            language = row[Languages.language]
        )
}