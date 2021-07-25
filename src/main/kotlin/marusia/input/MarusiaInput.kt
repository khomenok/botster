package marusia.input

import logic.LogicInput
import marusia.InputSession
import marusia.Meta

data class MarusiaInput (
    val meta: Meta,
    val request: Request,
    val session: InputSession,
    val version: String,
) : LogicInput {
    // messages from one sender in different chats probably should go to different logic entities
    override fun logicSenderHash() = this.session.sessionId
}