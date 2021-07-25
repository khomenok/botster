package ru.kotmarusia.api

data class Output (
    val response: Response,
    val session: InputSession,
    val version: String = "1.0",
)