package com.android.enoticoncreaterkotlin.widget.dialog

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.android.enoticoncreaterkotlin.R

class ProgressDialog(context: Context) {

    private var alertDialog: AlertDialog? = null
    private var tvMessage: TextView? = null
    private var onDismissListener: OnDismissListener? = null

    init {
        val inflater = LayoutInflater.from(context)
        val rootView = inflater.inflate(R.layout.dialog_progress, null)
        tvMessage = rootView.findViewById(R.id.tv_message)

        val builder = AlertDialog.Builder(context)
        builder.setView(rootView)
        builder.setCancelable(true)
        builder.setOnDismissListener {
            onDismissListener?.onDismiss()
        }

        alertDialog = builder.create()
    }

    fun setCancelable(cancelable: Boolean) {
        alertDialog?.setCancelable(cancelable)
        alertDialog?.setCanceledOnTouchOutside(cancelable)
    }

    fun setMessage(message: String) {
        tvMessage?.text = message
    }

    fun setOnkeyListener(onkeyListener: DialogInterface.OnKeyListener) {
        alertDialog?.setOnKeyListener(onkeyListener)
    }

    fun setOnDismissListener(onDismissListener: OnDismissListener) {
        this.onDismissListener = onDismissListener
    }

    fun show() {
        if (alertDialog != null && !alertDialog?.isShowing!!) {
            alertDialog?.show()
        }
    }

    fun dismiss() {
        if (alertDialog != null && alertDialog?.isShowing!!) {
            alertDialog?.dismiss()
        }
    }

    interface OnDismissListener {
        fun onDismiss()
    }
}