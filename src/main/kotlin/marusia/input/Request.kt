package marusia.input

import com.google.gson.annotations.SerializedName
import marusia.ButtonPayload

data class Request (
    val command: String,
    @SerializedName("original_utterance") val originalUtterance: String,
    val type: String,
    val payload: ButtonPayload,
    val nlu: ParsedTokens,
)
