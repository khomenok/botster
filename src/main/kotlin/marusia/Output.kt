package marusia

class Output (
    val response: Response,
    inputMessage: Input,
) {
    val session = inputMessage.session
    val version = inputMessage.version
}