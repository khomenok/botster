package telegram.output

import com.google.gson.annotations.SerializedName

open class InlineKeyboardButton (
    @SerializedName("text") val text: String = "",
    @SerializedName("callback_data") val callbackData: String? = null, // todo: add constraint 64b

    @SerializedName("url") val url: String? = null,
    // @SerializedName("login_url") val login_url: String? = null, // todo: api support
    @SerializedName("switch_inline_query") val switchInlineQuery: String? = null,
    @SerializedName("switch_inline_query_current_chat") val switchInlineQueryCurrentChat: String? = null,
    // @SerializedName("callback_game") val callbackGame: Any? = null, // todo: api support

    @SerializedName("pay") val pay: Boolean? = null, // must be the first button in first row
)

class TextInlineKeyboardButton (
    text: String,
    callbackData: String,
) : InlineKeyboardButton(text, callbackData = callbackData)

typealias InlineKeyboardButtons = List<List<InlineKeyboardButton>>

data class InlineKeyboard (
    @SerializedName("inline_keyboard") val buttons: InlineKeyboardButtons
)