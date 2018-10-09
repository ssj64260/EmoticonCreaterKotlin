package com.android.enoticoncreaterkotlin.app

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.android.enoticoncreaterkotlin.widget.dialog.ProgressDialog

abstract class BaseFragment : Fragment() {

    protected var mFragmentView: View? = null

    private var mProgressDialog: ProgressDialog? = null
    private var manager: InputMethodManager? = null

    protected var mActivity: Activity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mFragmentView = inflater.inflate(getLayoutId(), container, false)

        initView(savedInstanceState)

        return mFragmentView
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.mActivity = context as Activity
    }

    override fun onDetach() {
        super.onDetach()
        mActivity = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideProgress()
    }

    protected fun initData(savedInstanceState: Bundle?) {
        manager = mActivity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    protected fun initView(savedInstanceState: Bundle?) {

    }

    protected fun showProgress(@StringRes stringId: Int) {
        showProgress(getString(stringId))
    }

    protected fun showProgress(text: String) {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog(mActivity!!)
            mProgressDialog?.setCancelable(false)
        }

        mProgressDialog?.setMessage(text)
        mProgressDialog?.show()
    }

    protected fun hideProgress() {
        mProgressDialog?.dismiss()
    }

    @LayoutRes
    abstract protected fun getLayoutId(): Int
}