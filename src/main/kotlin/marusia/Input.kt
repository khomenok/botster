package ru.kotmarusia.api

data class Input (
    val meta: Meta,
    val request: Request,
    val session: InputSession,
    val version: String,
)