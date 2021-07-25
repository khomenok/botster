package ru.kotmarusia.api

enum class MetaInterface { screen }

data class Meta (
    val locale: String,
    val timezone: String,
    val metaInterfaces: List<MetaInterface>
)