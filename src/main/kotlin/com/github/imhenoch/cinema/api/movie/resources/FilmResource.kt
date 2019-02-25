package com.github.imhenoch.cinema.api.movie.resources

import com.github.imhenoch.cinema.api.movie.models.Film
import com.github.imhenoch.cinema.api.movie.services.FilmService
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

fun Route.film(filmService: FilmService) {
    route("/film") {
        get("/") {
            call.respond(filmService.findAll())
        }

        get("/{id}") {
            val film = filmService.findBy(call.parameters["id"]?.toInt()!!)
            if (film == null)
                call.respond(HttpStatusCode.NotFound)
            else
                call.respond(film)
        }

        post("/") {
            val film = call.receive<Film>()
            call.respond(filmService.create(film))
        }

        put("/") {
            val film = call.receive<Film>()
            val updatedFilm = filmService.save(film)
            if (updatedFilm == null)
                call.respond(HttpStatusCode.NotFound)
            else
                call.respond(updatedFilm)
        }

        delete("/{id}") {
            val removedFilm = filmService.delete(call.parameters["id"]?.toInt()!!)
            if (removedFilm)
                call.respond(HttpStatusCode.OK)
            else
                call.respond(HttpStatusCode.NotFound)
        }
    }
}