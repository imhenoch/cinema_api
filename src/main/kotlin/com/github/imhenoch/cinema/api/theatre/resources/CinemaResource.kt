package com.github.imhenoch.cinema.api.theatre.resources

import com.github.imhenoch.cinema.api.theatre.models.Cinema
import com.github.imhenoch.cinema.api.theatre.services.CinemaService
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

fun Route.cinema(cinemaService: CinemaService) {
    route("/cinema") {
        get("/") {
            call.respond(cinemaService.findAll())
        }

        get("/{id}") {
            val cinema = cinemaService.findBy(call.parameters["id"]?.toInt()!!)
            if (cinema == null)
                call.respond(HttpStatusCode.NotFound)
            else
                call.respond(cinema)
        }

        post("/") {
            val cinema = call.receive<Cinema>()
            call.respond(cinemaService.create(cinema))
        }

        put("/") {
            val cinema = call.receive<Cinema>()
            val updatedCinema = cinemaService.save(cinema)
            if (updatedCinema == null)
                call.respond(HttpStatusCode.NotFound)
            else
                call.respond(updatedCinema)
        }

        delete("/{id}") {
            val removedCinema = cinemaService.delete(call.parameters["id"]?.toInt()!!)
            if (removedCinema)
                call.respond(HttpStatusCode.OK)
            else
                call.respond(HttpStatusCode.NotFound)
        }
    }
}