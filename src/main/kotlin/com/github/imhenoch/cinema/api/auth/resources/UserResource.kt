package com.github.imhenoch.cinema.api.auth.resources

import com.github.imhenoch.cinema.api.auth.models.User
import com.github.imhenoch.cinema.api.auth.services.UserService
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.routing.route

fun Route.user(userService: UserService) {
    route("/user") {
        post("/") {
            val user = call.receive<User>()
            call.respond(userService.create(user))
        }
    }
}