package telegram.output

import io.ktor.client.statement.*
import telegram.Api

interface OutputEntity {
    suspend fun getSent(api: Api): HttpResponse
}