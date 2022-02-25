package me.nyaken.httpconnection.home

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

@BindingAdapter("image_url")
fun setImageUrl(
    view: ImageView,
    url: String?
) {
    url?.let {
        view.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.Main).launch {
            val bitmap = withContext(Dispatchers.IO) {
                loadImage(url)
            }
            view.setImageBitmap(bitmap)
        }
    } ?: run {
        view.visibility = View.GONE
    }
}

@BindingAdapter("item_html_text")
fun TextView.setHtmlText(
    item: String?
) {
    item?.let {
        text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(it, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(it)
        }
    }
}

private fun loadImage(url: String): Bitmap? {
    return try {
        val connection: HttpURLConnection = URL(url).openConnection() as HttpURLConnection
        connection.doInput = true
        connection.connect()
        val input: InputStream = connection.inputStream
        BitmapFactory.decodeStream(input)
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}