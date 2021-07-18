package telegram

import com.google.gson.annotations.SerializedName

data class ChatPermissions (
    @SerializedName("can_send_messages") val canSendMessages: Boolean?,
    @SerializedName("can_send_media_messages") val canSendMediaMessages: Boolean?,
    @SerializedName("can_send_polls") val canSendPolls: Boolean?,
    @SerializedName("can_send_other_messages") val canSendOtherMessages: Boolean?,
    @SerializedName("can_add_web_page_previews") val canAddWebPagePreviews: Boolean?,
    @SerializedName("can_change_info") val canChangeInfo: Boolean?,
    @SerializedName("can_invite_users") val canInviteUsers: Boolean?,
    @SerializedName("can_pin_messages") val canPinMessages: Boolean?,
)