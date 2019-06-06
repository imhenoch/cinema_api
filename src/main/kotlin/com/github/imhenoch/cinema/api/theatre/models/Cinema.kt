package com.github.imhenoch.cinema.api.theatre.models

import org.jetbrains.exposed.sql.Table

object Cinemas : Table() {
    val id = integer("id").primaryKey().autoIncrement()
    val name = varchar("name", length = 10)
    val row = integer("rows")
    val column = integer("columns")
}

data class Cinema(
    val id: Int?,
    val name: String,
    val row: Int,
    val column: Int
)