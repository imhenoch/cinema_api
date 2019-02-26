package com.github.imhenoch.cinema.api

import com.fasterxml.jackson.databind.SerializationFeature
import com.github.imhenoch.cinema.api.auth.resources.user
import com.github.imhenoch.cinema.api.auth.services.UserService
import com.github.imhenoch.cinema.api.common.DatabaseFactory
import com.github.imhenoch.cinema.api.movie.resources.film
import com.github.imhenoch.cinema.api.movie.resources.genre
import com.github.imhenoch.cinema.api.movie.resources.language
import com.github.imhenoch.cinema.api.movie.resources.rating
import com.github.imhenoch.cinema.api.movie.services.FilmService
import com.github.imhenoch.cinema.api.movie.services.GenreService
import com.github.imhenoch.cinema.api.movie.services.LanguageService
import com.github.imhenoch.cinema.api.movie.services.RatingService
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.jackson.jackson
import io.ktor.network.tls.certificates.generateCertificate
import io.ktor.routing.Routing
import java.io.File

fun Application.main() {
    install(DefaultHeaders)
    install(CallLogging)

    install(ContentNegotiation) {
        jackson {
            configure(SerializationFeature.INDENT_OUTPUT, true)
        }
    }

    DatabaseFactory.init()

    install(Routing) {
        language(LanguageService())
        genre(GenreService())
        rating(RatingService())
        film(FilmService())
        user(UserService())
    }
}

@Suppress("UnusedMainParameter")
class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val file = File("build/temporary.jks")

            if (!file.exists()) {
                file.parentFile.mkdirs()
                generateCertificate(file)
            }
        }
    }
}