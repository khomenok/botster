package telegram.input

import com.google.gson.annotations.SerializedName
import logic.LogicInput
import telegram.ChatId

data class TelegramInput (
    @SerializedName("update_id") val updateId: Int,
    @SerializedName("message") val message: Message? = null,
    @SerializedName("edited_message") val editedMessage: Message? = null,
    @SerializedName("callback_query") val callbackQuery: CallbackQuery? = null,
): LogicInput {
    fun senderChatId(): ChatId { // todo: complete
        return listOf(
            message,
            editedMessage,
            callbackQuery,
        ).firstOrNull { it != null }?.senderChatId() ?: 0
    }

    fun senderId(): ChatId {
        return listOf(
            message,
            editedMessage,
            callbackQuery,
        ).firstOrNull { it != null }?.senderId() ?: 0
    }

    // messages from one sender in different chats probably should go to different logic entities
    override fun logicSenderHash() = "${this.senderChatId()}-${this.senderId()}"
}

data class TelegramInputs (
    val ok: Boolean,
    val result: List<TelegramInput>,
)