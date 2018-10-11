package com.android.enoticoncreaterkotlin.widget.imageloader

class ImageLoaderFactory private constructor() {

    companion object {
        val instance: ImageLoaderWrapper by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ImageLoader()
        }
    }

}