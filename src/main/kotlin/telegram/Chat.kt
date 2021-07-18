package telegram

import com.google.gson.annotations.SerializedName

typealias ChatId = Int

enum class ChatType { private, group, supergroup, channel }

data class Chat (
    val id: ChatId,
    val type: ChatType,
    val title: String?,

    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String?,
    @SerializedName("username") val username: String?,
    @SerializedName("bio") val bio: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("photo") val photo: Any?, // todo:

    @SerializedName("invite_link") val inviteLink: String?,
    @SerializedName("pinned_message") val pinnedMessage: Any?, // todo:
    @SerializedName("permissions") val permissions: ChatPermissions?,

    @SerializedName("slow_mode_delay") val slowModeDelay: Int?,
    @SerializedName("message_auto_delete_time") val messageAutoDeleteTime: Int?,
    @SerializedName("sticker_set_name") val stickerSetName: String?,
    @SerializedName("can_set_sticker_set") val canSetStickerSet: Boolean?,
    @SerializedName("linked_chat_id") val linkedChatId: Int?,

    // @SerializedName("location") val location: Location?, // todo:
)