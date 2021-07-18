package telegram

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import telegram.input.Updates
import telegram.output.OutputEditMessageText
import telegram.output.OutputMessage

class Api (
    token: String,
    telegramApiUri: String = "https://api.telegram.org",
) {
    private val client = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = GsonSerializer()
        }
    }

    private val apiUri = "$telegramApiUri/bot$token"
    private fun getMethodUri(method: String) = "$apiUri/$method"

    private var lastUpdateId = 0

    suspend fun getUpdates(): Updates {
        val response: HttpResponse = client.request("${getMethodUri("getUpdates")}?offset=$lastUpdateId")
        val updates = response.receive<Updates>()

        if (updates.ok && updates.result.isNotEmpty()) {
            lastUpdateId = updates.result.maxOf { it.updateId } + 1
        }
        return updates
    }

    suspend fun sendMessage(message: OutputMessage): HttpResponse {
        return client.request(getMethodUri("sendMessage")) {
            contentType(ContentType.Application.Json)
            method = HttpMethod.Post
            body = message
        }
    }

    suspend fun editMessageText(editMessageEntity: OutputEditMessageText): HttpResponse {
        return client.request(getMethodUri("editMessageText")) {
            contentType(ContentType.Application.Json)
            method = HttpMethod.Post
            body = editMessageEntity
        }
    }
}