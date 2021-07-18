package telegram.input

import com.google.gson.annotations.SerializedName
import telegram.ChatId
import telegram.User

data class CallbackQuery (
    @SerializedName("id") val id: String,
    @SerializedName("user") val user: User,
    @SerializedName("data") val data: String?,
    @SerializedName("message") val message: Message? = null,

    @SerializedName("inline_message_id") val inlineMessageId: String? = null,
    @SerializedName("chat_instance") val chatInstance: String? = null,
    @SerializedName("game_short_name") val gameShortName: String? = null,
) : InputEntity {

    // todo: decide, to which part of logic should go callbacks to another pupils' messages
    override fun senderChatId(): ChatId {
        return message?.chat?.id ?: 0 // todo: handle nulls
    }

    override fun senderId(): ChatId {
        return message?.from?.id ?: 0
    }
}