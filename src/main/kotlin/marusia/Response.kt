package ru.kotmarusia.api

import com.google.gson.annotations.SerializedName

data class Response (
    val text: List<String>,
    @SerializedName("end_session") val endSession: Boolean = false,

    val buttons: List<Button>? = null,
    val tts: Any? = null, // todo:
    val card: Any? = null, // todo:
)