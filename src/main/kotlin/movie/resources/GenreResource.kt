package movie.resources

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
import movie.models.Genre
import movie.services.GenreService

fun Route.genre(genreService: GenreService) {
    route("/genre") {
        get("/") {
            call.respond(genreService.getAllGenres())
        }

        get("/{id}") {
            val genre = genreService.getGenre(call.parameters["id"]?.toInt()!!)
            if (genre == null)
                call.respond(HttpStatusCode.NotFound)
            else
                call.respond(genre)
        }

        post("/") {
            val genre = call.receive<Genre>()
            call.respond(genreService.addGenre(genre))
        }

        put("/") {
            val genre = call.receive<Genre>()
            val updatedGenre = genreService.updateGenre(genre)
            if (updatedGenre == null)
                call.respond(HttpStatusCode.NotFound)
            else
                call.respond(HttpStatusCode.OK, updatedGenre)
        }

        delete("/{id}") {
            val removedGenre = genreService.deleteGenre(call.parameters["id"]?.toInt()!!)
            if (removedGenre)
                call.respond(HttpStatusCode.OK)
            else
                call.respond(HttpStatusCode.NotFound)
        }
    }
}