package com.github.imhenoch.cinema.api.movie.resources

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
import com.github.imhenoch.cinema.api.movie.models.Genre
import com.github.imhenoch.cinema.api.movie.services.GenreService

fun Route.genre(genreService: GenreService) {
    route("/genre") {
        get("/") {
            call.respond(genreService.findAll())
        }

        get("/{id}") {
            val genre = genreService.findBy(call.parameters["id"]?.toInt()!!)
            if (genre == null)
                call.respond(HttpStatusCode.NotFound)
            else
                call.respond(genre)
        }

        post("/") {
            val genre = call.receive<Genre>()
            call.respond(genreService.create(genre))
        }

        put("/") {
            val genre = call.receive<Genre>()
            val updatedGenre = genreService.save(genre)
            if (updatedGenre == null)
                call.respond(HttpStatusCode.NotFound)
            else
                call.respond(HttpStatusCode.OK, updatedGenre)
        }

        delete("/{id}") {
            val removedGenre = genreService.delete(call.parameters["id"]?.toInt()!!)
            if (removedGenre)
                call.respond(HttpStatusCode.OK)
            else
                call.respond(HttpStatusCode.NotFound)
        }
    }
}