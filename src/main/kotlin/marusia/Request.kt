package marusia

import com.google.gson.annotations.SerializedName

data class Request (
    val command: String,
    @SerializedName("original_utterance") val originalUtterance: String,
    val type: String,
    val payload: ButtonPayload,
    val nlu: ParsedTokens,
)
