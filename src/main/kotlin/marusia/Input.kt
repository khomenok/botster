package marusia

import logic.LogicInput

data class Input (
    val meta: Meta,
    val request: Request,
    val session: InputSession,
    val version: String,
) : LogicInput {
    // messages from one sender in different chats probably should go to different logic entities
    override fun logicSenderHash() = this.session.sessionId
}