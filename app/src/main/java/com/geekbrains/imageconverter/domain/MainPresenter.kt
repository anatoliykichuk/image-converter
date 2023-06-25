package com.geekbrains.imageconverter.domain

import android.graphics.Bitmap
import android.net.Uri
import com.geekbrains.imageconverter.data.ImageConverter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import moxy.MvpPresenter

class MainPresenter : MvpPresenter<MainView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
    }

    fun showImagePicker() {
        viewState.showImagePicker()
    }

    fun onImageSelected(imageUri: Uri) {
        viewState.onImageSelected(imageUri)
    }

    fun convertImageToPng(image: Bitmap, imageName: String) {
        val conversionObserver = object : Observer<Boolean> {
            var disposable: Disposable? = null

            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onNext(isImageConverted: Boolean) {}

            override fun onError(e: Throwable) {}

            override fun onComplete() {}
        }

        ImageConverter().isConvertedToPng(image, imageName)
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(conversionObserver)
    }
}