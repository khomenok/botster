package telegram

import com.google.gson.annotations.SerializedName

data class User (
    val id: ChatId,

    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String?,
    @SerializedName("username") val username: String?,

    @SerializedName("language_code") val languageCode: String?,

    @SerializedName("is_bot") val isBot: Boolean,
    @SerializedName("can_join_groups") val canJoinGroups: Boolean?,
    @SerializedName("can_read_all_messages") val canReadAllGroupMessages: Boolean?,
    @SerializedName("supports_inline_queries") val supportsInlineQueries: Boolean?,
)