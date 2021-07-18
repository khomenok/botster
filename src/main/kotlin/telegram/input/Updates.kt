package telegram.input

import com.google.gson.annotations.SerializedName
import telegram.ChatId

data class Update (
    @SerializedName("update_id") val updateId: Int,
    @SerializedName("message") val message: Message? = null,
    @SerializedName("edited_message") val editedMessage: Message? = null,
    @SerializedName("callback_query") val callbackQuery: CallbackQuery? = null,
) {
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
}

data class Updates (
    val ok: Boolean,
    val result: List<Update>,
)