package com.github.imhenoch.cinema.api.movie.services

import com.github.imhenoch.cinema.api.common.DatabaseFactory.query
import com.github.imhenoch.cinema.api.movie.models.Rating
import com.github.imhenoch.cinema.api.movie.models.Ratings
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

class RatingService {
    suspend fun getAllRatings(): List<Rating> = query {
        Ratings
            .selectAll()
            .toRatings()
    }

    suspend fun getRating(id: Int): Rating? = query {
        Ratings
            .select {
                (Ratings.id eq id)
            }
            .toRatings()
            .singleOrNull()
    }

    suspend fun addRating(rating: Rating): Rating {
        var key = 0
        query {
            key = (Ratings.insert {
                it[Ratings.rating] = rating.rating
            } get Ratings.id)!!
        }
        return getRating(key)!!
    }

    suspend fun updateRating(rating: Rating): Rating? {
        val id = rating.id
        return if (id == null) {
            null
        } else {
            query {
                Ratings.update({ Ratings.id eq id }) {
                    it[Ratings.rating] = rating.rating
                }
            }
            getRating(id)
        }
    }

    suspend fun deleteRating(id: Int): Boolean = query {
        Ratings.deleteWhere { Ratings.id eq id } > 0
    }
}

fun Iterable<ResultRow>.toRatings() =
    this.map(ResultRow::toRating)

fun ResultRow.toRating() = Rating(
    id = this[Ratings.id],
    rating = this[Ratings.rating]
)