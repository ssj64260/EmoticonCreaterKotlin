package com.android.enoticoncreaterkotlin.widget.imageloader

import android.content.Context
import android.widget.ImageView

interface ImageLoaderWrapper {

    fun loadImage(context: Context, imageView: ImageView, url: Any)

    fun loadImage(context: Context, imageView: ImageView, url: Any, placeholder: Int, errorImage: Int)

    fun loadImageFitCenter(context: Context, imageView: ImageView, url: Any)

    fun loadImageFitCenter(context: Context, imageView: ImageView, url: Any, placeholder: Int, errorImage: Int)

    fun loadImageCenterCrop(context: Context, imageView: ImageView, url: Any)

    fun loadImageCenterCrop(context: Context, imageView: ImageView, url: Any, placeholder: Int, errorImage: Int)

}