package language

import org.jetbrains.exposed.sql.Table

object Languages : Table() {
    val id = integer("id").primaryKey().autoIncrement()
    val language = varchar("language", 50)
}

data class Language(
    val id: Int,
    val language: String
)