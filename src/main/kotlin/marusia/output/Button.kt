package marusia.output

typealias ButtonPayload = Any // todo: implement

data class Button (
    val title: String,
    val url: String? = null,
    val payload: ButtonPayload? = null,
)
