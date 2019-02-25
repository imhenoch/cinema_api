package com.github.imhenoch.cinema.api.movie.resources

import com.github.imhenoch.cinema.api.movie.models.Rating
import com.github.imhenoch.cinema.api.movie.services.RatingService
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.delete
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.put
import io.ktor.routing.route

fun Route.rating(ratingService: RatingService) {
    route("/rating") {
        get("/") {
            call.respond(ratingService.findAll())
        }

        get("/{id}") {
            val rating = ratingService.findBy(call.parameters["id"]?.toInt()!!)
            if (rating == null)
                call.respond(HttpStatusCode.NotFound)
            else
                call.respond(rating)
        }

        post("/") {
            val rating = call.receive<Rating>()
            call.respond(ratingService.create(rating))
        }

        put("/") {
            val rating = call.receive<Rating>()
            val updatedRating = ratingService.save(rating)
            if (updatedRating == null)
                call.respond(HttpStatusCode.NotFound)
            else
                call.respond(HttpStatusCode.OK, updatedRating)
        }

        delete("/{id}") {
            val removedRating = ratingService.delete(call.parameters["id"]?.toInt()!!)
            if (removedRating)
                call.respond(HttpStatusCode.OK)
            else
                call.respond(HttpStatusCode.NotFound)
        }
    }
}