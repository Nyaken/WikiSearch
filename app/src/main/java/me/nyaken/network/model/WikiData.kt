package me.nyaken.network.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class WikiData(
    @SerializedName("title")
    val title: String,
    @SerializedName("extract")
    val extract: String,
    @SerializedName("thumbnail")
    val thumbnail: ThumbnailData
): Parcelable