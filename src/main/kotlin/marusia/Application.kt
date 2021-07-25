package marusia

import com.google.gson.annotations.SerializedName

enum class ApplicationType { mobile, speaker, VK, other }

data class Application (
    @SerializedName("application_id") val applicationId: String,
    @SerializedName("application_type") val applicationType: ApplicationType,
)