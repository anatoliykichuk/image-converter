package com.geekbrains.imageconverter.data

import android.graphics.Bitmap
import android.os.Environment
import io.reactivex.rxjava3.core.Observable
import java.io.File
import java.io.FileOutputStream

class ImageConverter {

    fun isConvertedToPng(image: Bitmap, imageName: String): Observable<Boolean> {
        val outputStream = FileOutputStream(
            File(
                Environment
                    .getExternalStorageDirectory()
                    .toString() + "/Pictures", "$imageName.png"
            )
        )
        val result = image.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return Observable.just(result)
    }
}