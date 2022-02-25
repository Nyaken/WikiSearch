package me.nyaken.network.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SummaryResponse(
    @SerializedName("displaytitle")
    val displaytitle: String,
    @SerializedName("pageid")
    val pageid: Long,
    @SerializedName("extract_html")
    val extract_html: String,
    @SerializedName("thumbnail")
    val thumbnail: ThumbnailData
): Parcelable