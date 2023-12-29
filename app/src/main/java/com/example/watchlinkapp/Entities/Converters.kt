package com.example.watchlinkapp.Entities

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

class Converters {
    @TypeConverter
    fun fromByteArray(byteArray: ByteArray): String {
        return byteArray.toString(Charsets.ISO_8859_1)
    }

    @TypeConverter
    fun toByteArray(value: String): ByteArray {
        return value.toByteArray(Charsets.ISO_8859_1)
    }

    fun imageResourceToByteArray(context: Context, resId: Int): ByteArray {
        val bitmap = BitmapFactory.decodeResource(context.resources, resId)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
}