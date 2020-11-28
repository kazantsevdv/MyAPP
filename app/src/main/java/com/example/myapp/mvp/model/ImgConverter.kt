package com.example.myapp.mvp.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File
import java.io.FileNotFoundException
import java.io.OutputStream


class ImgConverter(val context: Context) : IConvrter {

    override fun convertImg(uriString: String): Completable {
        return Completable.create {
            convert(uriString)
            it.onComplete()
        }.subscribeOn(Schedulers.io())
    }

    private fun convert(uriString: String) {
        //val input: InputStream? = context.getContentResolver().openInputStream(Uri.parse(currentPhotoPath))
        val input = File(Uri.parse(uriString).path)
        val bmOptions = BitmapFactory.Options()
        val bitmap = BitmapFactory.decodeFile(input.toString())
        val tpath: String = uriString.replace(".jpg", ".png")
        val pathtoPng = tpath.replace("JPEG", "PNG")
        var out: OutputStream? = null
        try {
//            out = context.getContentResolver().openOutputStream(Uri.parse(uriString))
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        try {
            Thread.sleep(5000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

}