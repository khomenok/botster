package telegram.input

import com.google.gson.annotations.SerializedName
import telegram.Chat
import telegram.User

typealias MessageId = Int

data class Message (
    @SerializedName("message_id") val messageId: MessageId,
    @SerializedName("from") val from: User?,
    @SerializedName("sender_chat") val senderChat: Chat?,
    @SerializedName("chat") val chat: Chat,

    @SerializedName("date") val date: Int,

    @SerializedName("forward_from") val forwardFrom: User?,
    @SerializedName("forward_from_chat") val forwardFromChat: Chat?,
    @SerializedName("forward_from_message_id") val forwardFromMessageId: Int?,
    @SerializedName("forward_signature") val forwardSignature: String?,
    @SerializedName("forward_sender_name") val forwardSenderName: String?,
    @SerializedName("forward_date") val forwardDate: Int?,

    @SerializedName("reply_to_message") val replyToMessage: Message?,

    @SerializedName("via_bot") val viaBot: User?,

    @SerializedName("text") val text: String?,
) : InputEntity {
    override fun senderChatId() = chat.id
    override fun senderId() = from?.id ?: 0 // todo: support empty id
}

data class TextMessage (
    val message: Message,
) {
    val text = message.text!!
}