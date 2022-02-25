package me.nyaken.network.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ThumbnailData(
    @SerializedName("source")
    val source: String,
    @SerializedName("width")
    val width: Int,
    @SerializedName("height")
    val height: Int
): Parcelable