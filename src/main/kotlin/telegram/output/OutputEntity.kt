package telegram.output

import telegram.Api
import telegram.ChatId

interface OutputEntity {
    val chatId: ChatId
    val disableNotification: Boolean?

    val replyToMessageId: Int?
    val allowSendingWithoutReply: Boolean?

    suspend fun getSent(api: Api)
}