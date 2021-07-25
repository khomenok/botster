package marusia

enum class MetaInterface { screen }

data class Meta (
    val locale: String,
    val timezone: String,
    val metaInterfaces: List<MetaInterface>
)