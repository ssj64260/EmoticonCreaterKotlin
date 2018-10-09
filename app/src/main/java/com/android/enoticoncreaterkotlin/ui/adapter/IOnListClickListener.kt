package com.android.enoticoncreaterkotlin.ui.adapter

import android.view.View
import androidx.annotation.IntDef

interface IOnListClickListener {

    @IntDef(ITEM_TAG0, ITEM_TAG1, ITEM_TAG2)
    @Retention(AnnotationRetention.SOURCE)
    annotation class ItemView

    companion object {
        const val ITEM_TAG0 = 0
        const val ITEM_TAG1 = 1
        const val ITEM_TAG2 = 2
    }


    //item点击事件
    fun onItemClick(position: Int)

    //item点击事件，返回model
    fun onItemClick(obj: Any)

    fun onItemClick(view: View, position: Int)

    fun onItemClick(view: View, obj: Any)

    //可根据tag来区分点击的是item内部哪个控件
    fun onTagClick(@ItemView tag: Int, position: Int)
}