package com.github.imhenoch.cinema.api.auth.resources

import com.github.imhenoch.cinema.api.auth.common.JwtConfig
import com.github.imhenoch.cinema.api.auth.models.User
import com.github.imhenoch.cinema.api.auth.services.LoginService
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.routing.route
import org.mindrot.jbcrypt.BCrypt

fun Route.login(loginService: LoginService) {
    route("/") {
        post("/login") {
            val user = call.receive<User>()
            val databaseUser = loginService.findBy(user.email)
            if (BCrypt.checkpw(user.password, databaseUser?.password))
                call.respond(
                    JwtConfig.LoggedUser(
                        databaseUser!!.id!!,
                        databaseUser.email,
                        JwtConfig.generateToken(databaseUser)
                    )
                )
            else
                call.respond((HttpStatusCode.Unauthorized))
        }
    }
}