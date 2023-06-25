package com.geekbrains.imageconverter.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.geekbrains.imageconverter.databinding.ActivityMainBinding
import com.geekbrains.imageconverter.domain.MainPresenter
import com.geekbrains.imageconverter.domain.MainView
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter


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
            presenter.showImagePicker()
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
                data?.getData()?.let {
                    presenter.onImageSelected(it)
                }
            }
        }
    }

    override fun showImagePicker() {
        val imagePickerIntent = Intent(Intent.ACTION_PICK).apply {
            setType("image/jpeg")
        }
        startActivityForResult(imagePickerIntent, GALLERY_REQUEST_CODE)
    }

    override fun onImageSelected(imageUri: Uri) {
        val imageName = imageUri.getLastPathSegment().toString()
        val image = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri)

        presenter.convertImageToPng(image, imageName)
    }

    override fun showResultMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}