package marusia

import com.google.gson.annotations.SerializedName

typealias UserId = String

data class User (
    @SerializedName("user_id") val userId: UserId,
)