package ru.kotmarusia.api

import com.google.gson.annotations.SerializedName

typealias SessionId = String
typealias MessageId = Int

data class InputSession (
    @SerializedName("session_id") val sessionId: SessionId,
    // val sessionId: String, deprecated
    @SerializedName("skill_id") val skillId: String,
    val new: Boolean,
    @SerializedName("message_id") val messageId: MessageId,
    @SerializedName("user_id") val userId: String,
    val application: Application,

    @SerializedName("auth_token") val authToken: String, // todo: ???
)

data class OutputSession (
    @SerializedName("session_id") val sessionId: SessionId,
    @SerializedName("user_id") val userId: UserId,
    @SerializedName("message_id") val messageId: MessageId,
)