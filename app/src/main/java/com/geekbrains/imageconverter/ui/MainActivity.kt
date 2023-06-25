package com.geekbrains.imageconverter.ui

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import com.geekbrains.imageconverter.databinding.ActivityMainBinding
import com.geekbrains.imageconverter.domain.MainPresenter
import com.geekbrains.imageconverter.domain.MainView
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


private const val GALLERY_REQUEST_CODE = 1

class MainActivity : MvpAppCompatActivity(), MainView {

    private val presenter by moxyPresenter { MainPresenter() }

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.convertImageButton.setOnClickListener {
            selectJpegImageFile()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            GALLERY_REQUEST_CODE -> {

                val selectedJpegImageFileUri = data?.getData()
                val selectedJpegImageFileName = selectedJpegImageFileUri?.getLastPathSegment()
                    .toString()

                val jpegImageFile = MediaStore.Images.Media
                    .getBitmap(getContentResolver(), selectedJpegImageFileUri)

                savePngImageFile(jpegImageFile, selectedJpegImageFileName)
            }
        }
    }

    private fun selectJpegImageFile() {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)

        photoPickerIntent.setType("image/jpeg")
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST_CODE)
    }

    private fun savePngImageFile(jpegImageFile: Bitmap, fileName: String) {
        var outputStream: FileOutputStream? = null
        var message: String = ""

        try {
            outputStream = FileOutputStream(
                File(
                    Environment
                        .getExternalStorageDirectory()
                        .toString() + "/Pictures", "$fileName.png"
                )
            )

            jpegImageFile.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            message = "The $fileName.png saved"

        } catch (e: IOException) {

            e.printStackTrace()
            message = e.message.toString()

        } finally {

            outputStream?.let {
                it.flush()
                it.close()
            }
        }

        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}