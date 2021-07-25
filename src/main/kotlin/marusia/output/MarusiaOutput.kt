package marusia.output

import marusia.input.MarusiaInput
import marusia.output.Response

class MarusiaOutput (
    val response: Response,
    inputMessage: MarusiaInput,
) {
    val session = inputMessage.session
    val version = inputMessage.version
}