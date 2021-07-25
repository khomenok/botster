package marusia_logic

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import logic.Logic
import marusia.input.MarusiaInput
import marusia.output.MarusiaOutput

class MarusiaWebhook<State> (
    private val logic: Logic<State, MarusiaInput, MarusiaOutput>,
    private val app: Application,
    private val webhookUri: String = "/webhook"
) {
    fun setup() {
        app.routing {
            post(webhookUri) {
                val processed = logic.process(call.receive())
                if (processed.output.size != 1) {
                    throw Exception("Marusia logic step should return exactly one respond")
                }
                call.respond(processed.output[0])
            }
        }
    }
}