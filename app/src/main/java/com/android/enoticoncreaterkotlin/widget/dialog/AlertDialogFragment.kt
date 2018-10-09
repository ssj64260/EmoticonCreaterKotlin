package com.android.enoticoncreaterkotlin.widget.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class AlertDialogFragment : DialogFragment() {

    private var mContext: Context? = null

    private var mTitle: String? = ""
    private var mMessage: String? = ""
    private var mConfirmButtonText: String? = ""
    private var mCancelButtonText: String? = ""
    private var mConfirmClick: DialogInterface.OnClickListener? = null
    private var mCancelClick: DialogInterface.OnClickListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context
    }

    override fun onDetach() {
        super.onDetach()
        mContext = null
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(mContext!!)
        builder.setTitle(mTitle)
        builder.setMessage(mMessage)
        builder.setPositiveButton(mConfirmButtonText, mConfirmClick)
        builder.setNegativeButton(mCancelButtonText, mCancelClick)

        return builder.create()
    }

    fun setTitle(title: String) {
        mTitle = title
    }

    fun setMessage(message: String) {
        mMessage = message
    }

    fun setConfirmButton(buttonText: String) {
        mConfirmButtonText = buttonText
        mConfirmClick = DialogInterface.OnClickListener { dialog, which -> }
    }

    fun setConfirmButton(buttonText: String, confirmClick: DialogInterface.OnClickListener) {
        mConfirmButtonText = buttonText
        mConfirmClick = confirmClick
    }

    fun setCancelButton(buttonText: String) {
        mCancelButtonText = buttonText
        mCancelClick = DialogInterface.OnClickListener { dialog, which -> }
    }

    fun setCancelButton(buttonText: String, cancelClick: DialogInterface.OnClickListener) {
        mCancelButtonText = buttonText
        mCancelClick = cancelClick
    }
}