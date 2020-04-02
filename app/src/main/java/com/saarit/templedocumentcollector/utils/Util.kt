package com.saarit.templedocumentcollector.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException


object Util {

    private val TAG = Util::class.java.simpleName

    val shouldLog = true

    fun log(tag: String, msg: String) {
        if (shouldLog) {
            Log.i(tag, msg)
        }

    }

    fun showToast(msg: String, length: Int, context: Context) {

        Toast.makeText(context, msg, length).show()

    }

    fun getMediaFileUri(context: Context, number: Int): Uri {

        return Uri.fromFile(getMediaFile(context, number))
        /*return FileProvider.getUriForFile(
            context,
            context.getApplicationContext().getPackageName() + ".provider",
            getMediaFile(context, number)!!
        );*/

    }

    fun getMediaFile(context: Context, number: Int): File? {
        Util.log(TAG, "About to create img_file")

        val imgDir = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "temple_document_collector")

        if (!imgDir.exists()) {
            if (!imgDir.mkdirs()) {

                showToast("Could not create Image Directory", Toast.LENGTH_SHORT, context)

                return null
            }
        }

        val imgFile = File(imgDir, "image_" + number + ".jpg")

        return imgFile
    }

    @Throws(IOException::class)
    fun rotateImage(uri: Uri, context: Context): Bitmap {
        log(TAG, "rotateImageWithPath()")
        /*var inputStream = context.getContentResolver().openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        log(TAG, "Decoded Image")*/

        val bitmap = BitmapFactory.decodeFile(uri.path)
        var ei = ExifInterface(uri.path)


        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            log(TAG, "Exif..inputstream")
            ei = ExifInterface(inputStream)

        } else {
            log(TAG, "Exif..path")
            ei = ExifInterface(uri.path)
        }*/
        val orientation = ei.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )

        log(TAG, "Orientation:$orientation")

        var rotatedBitmap: Bitmap
        when (orientation) {


            ExifInterface.ORIENTATION_ROTATE_90 -> rotatedBitmap = rotateImage(bitmap, 90f)

            ExifInterface.ORIENTATION_ROTATE_180 -> rotatedBitmap = rotateImage(bitmap, 180f)

            ExifInterface.ORIENTATION_ROTATE_270 -> rotatedBitmap = rotateImage(bitmap, 270f)

            ExifInterface.ORIENTATION_NORMAL -> rotatedBitmap = bitmap
            else -> rotatedBitmap = bitmap
        }

        log(TAG, "About to return rotated bitmap")
        return rotatedBitmap
    }

    fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        Util.log(TAG, "About to rotate bitmap...Angle:$angle")
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        )
    }


    fun deleteImageByPath(context: Context, path: String) {

        /*val imgDir = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "temple_document_collector")*/

        val imgFile= File(path)
        if (imgFile.exists()) {
            imgFile.delete()
        } else {
            throw FileNotFoundException("Image not found")
        }


    }

    fun emptyImageDir(context: Context) {

        val imgDir = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "temple_document_collector")


        if (imgDir.isDirectory()) {
            val children = imgDir.list()
            for (i in children.indices) {
                File(imgDir, children[i]).delete()
            }
        }


    }

    fun getImageByPath(context: Context, path: String): File {

        /* val imgDir = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "temple_document_collector")*/

        val imgFile = File(path)
        if (imgFile.exists()) {
            return imgFile
        } else {
            throw FileNotFoundException("Some Image Missing..Please Take again")
        }


    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

}