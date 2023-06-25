package com.geekbrains.imageconverter.domain

import android.net.Uri
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface MainView : MvpView {
    fun showImagePicker()
    fun onImageSelected(imageUri: Uri)
    fun showResultMessage(message: String)
}