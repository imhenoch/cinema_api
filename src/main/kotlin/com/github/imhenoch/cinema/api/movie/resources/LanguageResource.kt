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
import com.github.imhenoch.cinema.api.movie.services.LanguageService
import com.github.imhenoch.cinema.api.movie.models.Language

fun Route.language(languageService: LanguageService) {
    route("/language") {
        get("/") {
            call.respond(languageService.getAllLanguages())
        }

        get("/{id}") {
            val language = languageService.getLanguage(call.parameters["id"]?.toInt()!!)
            if (language == null)
                call.respond(HttpStatusCode.NotFound)
            else
                call.respond(language)
        }

        post("/") {
            val language = call.receive<Language>()
            call.respond(languageService.addLanguage(language))
        }

        put("/") {
            val language = call.receive<Language>()
            val updatedLanguage = languageService.updateLanguage(language)
            if (updatedLanguage == null)
                call.respond(HttpStatusCode.NotFound)
            else
                call.respond(HttpStatusCode.OK, updatedLanguage)
        }

        delete("/{id}") {
            val removedLanguage = languageService.deleteLanguage(call.parameters["id"]?.toInt()!!)
            if (removedLanguage)
                call.respond(HttpStatusCode.OK)
            else
                call.respond(HttpStatusCode.NotFound)
        }
    }
}