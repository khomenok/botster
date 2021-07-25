package input

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.routing.*
import kotlinx.coroutines.flow.*
import ru.kotmarusia.api.Input

class MarusiaWebhookInput (
    private val app: Application,
    private val webhookUri: String = "/webhook"
) : FlowInput<Input> {
    override suspend fun setupFlow(): SharedFlow<Input> {
        val _inputFlow = MutableSharedFlow<Input>()

        app.routing {
            post(webhookUri) {
                _inputFlow.emit(call.receive())
            }
        }
        return _inputFlow.asSharedFlow()
    }

    override fun stop() {
        throw Exception("Marusia webhook can't be stopped in application for now")
    }
}