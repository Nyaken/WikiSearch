package me.nyaken.network.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class WikiData(
    @SerializedName("displaytitle")
    val title: String,
    @SerializedName("extract_html")
    val extract: String,
    @SerializedName("thumbnail")
    val thumbnail: ThumbnailData
): Parcelable