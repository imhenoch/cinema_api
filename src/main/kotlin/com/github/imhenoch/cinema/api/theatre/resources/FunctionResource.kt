package com.github.imhenoch.cinema.api.theatre.resources

import com.github.imhenoch.cinema.api.theatre.models.Function
import com.github.imhenoch.cinema.api.theatre.services.FunctionService
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

fun Route.function(functionService: FunctionService) {
    route("/function") {
        get("/") {
            call.respond(functionService.findAll())
        }

        get("/{id}") {
            val functions = functionService.findby(call.parameters["id"]?.toInt()!!)
            call.respond(functions)
        }

        post("/") {
            val function = call.receive<Function>()
            call.respond(functionService.create(function))
        }

        put("/") {
            val function = call.receive<Function>()
            val updatedFunction = functionService.save(function)
            if (updatedFunction == null)
                call.respond(HttpStatusCode.NotFound)
            else
                call.respond(updatedFunction)
        }

        delete("/{id}") {
            val removedFunction = functionService.delete(call.parameters["id"]?.toInt()!!)
            if (removedFunction)
                call.respond(HttpStatusCode.OK)
            else
                call.respond(HttpStatusCode.NotFound)
        }
    }
}