package marusia.output

typealias ButtonPayload = Any

data class Button (
    val title: String,
    val url: String? = null,
    val payload: ButtonPayload? = null,
)
