package telegram.output

import com.google.gson.annotations.SerializedName
import telegram.Api
import telegram.ChatId

class OutputMessage (
    @SerializedName("chat_id") val chatId: ChatId,

    @SerializedName("text") val text: String,

    @SerializedName("parse_mode") val parseMode: String? = null,
    // @SerializedName("entities") val entities: String? = null, // todo:

    @SerializedName("disable_web_page_preview") val disableWebPagePreview: Boolean? = null,
    @SerializedName("disable_notification") val disableNotification: Boolean? = null,

    @SerializedName("reply_to_message_id") val replyToMessageId: Int? = null,
    @SerializedName("allow_sending_without_reply") val allowSendingWithoutReply: Boolean? = null,

    @SerializedName("reply_markup") val replyMarkup: InlineKeyboard? = null,
): OutputEntity {
    override suspend fun getSent(api: Api) = api.sendMessage(this)
}