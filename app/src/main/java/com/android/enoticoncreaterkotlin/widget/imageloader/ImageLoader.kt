package com.android.enoticoncreaterkotlin.widget.imageloader

import android.content.Context
import android.widget.ImageView
import com.android.enoticoncreaterkotlin.R

class ImageLoader : ImageLoaderWrapper {

    private val mPlaceHolder = R.mipmap.ic_launcher
    private val mErrorImage = R.mipmap.ic_launcher

    override fun loadImage(context: Context, imageView: ImageView, url: Any) {
        loadImage(context, imageView, url, mPlaceHolder, mErrorImage)
    }

    override fun loadImage(context: Context, imageView: ImageView, url: Any, placeholder: Int, errorImage: Int) {
        GlideApp.with(context)
                .load(url)
                .placeholder(placeholder)
                .error(errorImage)
                .dontAnimate()
                .into(imageView)
    }

    override fun loadImageFitCenter(context: Context, imageView: ImageView, url: Any) {
        loadImageFitCenter(context, imageView, url, mPlaceHolder, mErrorImage)
    }

    override fun loadImageFitCenter(context: Context, imageView: ImageView, url: Any, placeholder: Int, errorImage: Int) {
        GlideApp.with(context)
                .load(url)
                .placeholder(placeholder)
                .error(errorImage)
                .fitCenter()
                .dontAnimate()
                .into(imageView)
    }

    override fun loadImageCenterCrop(context: Context, imageView: ImageView, url: Any) {
        loadImageCenterCrop(context, imageView, url, mPlaceHolder, mErrorImage)
    }

    override fun loadImageCenterCrop(context: Context, imageView: ImageView, url: Any, placeholder: Int, errorImage: Int) {
        GlideApp.with(context)
                .load(url)
                .placeholder(placeholder)
                .error(errorImage)
                .centerCrop()
                .dontAnimate()
                .into(imageView)
    }

}