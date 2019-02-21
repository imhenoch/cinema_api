import com.fasterxml.jackson.databind.SerializationFeature
import common.DatabaseFactory
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.jackson.jackson
import io.ktor.network.tls.certificates.generateCertificate
import io.ktor.routing.Routing
import movie.services.LanguageService
import movie.resources.language
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