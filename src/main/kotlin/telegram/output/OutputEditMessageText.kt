package telegram.output

import com.google.gson.annotations.SerializedName
import telegram.Api
import telegram.ChatId

open class OutputEditMessageText (
    // todo: read about options here
    @SerializedName("message_id") val messageId: Int? = null,
    @SerializedName("chat_id") val chatId: ChatId? = null,
    @SerializedName("inline_message_id") val inlineMessageId: Int? = null,

    @SerializedName("text") val text: String = "",

    @SerializedName("parse_mode") val parseMode: String? = null,
    // @SerializedName("entities") val entities: String? = null, // todo:
    @SerializedName("disable_web_page_preview") val disableWebPagePreview: Boolean? = null,

    @SerializedName("reply_markup") val replyMarkup: InlineKeyboard? = null,
) : OutputEntity {
    override suspend fun getSent(api: Api) = api.editMessageText(this)
}

class OutputEditBasicMessageText (
    messageId: Int,
    chatId: ChatId,
    text: String,

    parseMode: String? = null,
    disableWebPagePreview: Boolean? = null,
    replyMarkup: InlineKeyboard? = null,
) : OutputEditMessageText(
    messageId = messageId,
    chatId = chatId,
    text = text,
    parseMode = parseMode,
    disableWebPagePreview = disableWebPagePreview,
    replyMarkup = replyMarkup,
)