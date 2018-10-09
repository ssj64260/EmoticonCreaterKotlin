package com.android.enoticoncreaterkotlin.app

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.viewpager.widget.ViewPager
import com.android.enoticoncreaterkotlin.R
import com.android.enoticoncreaterkotlin.widget.dialog.ProgressDialog
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout

abstract class BaseActivity : AppCompatActivity() {

    private var manager: InputMethodManager? = null
    protected var mRootView: CoordinatorLayout? = null
    protected var mAppBarLayout: AppBarLayout? = null
    protected var mToolbar: Toolbar? = null
    protected var mTabLayout: TabLayout? = null
    protected var mProgressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (getContentView() != 0) {
            setContentView(getContentView())
        }

        initData()
        initView(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        hideKeyboard()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    open fun initData() {
        manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    open fun initView(savedInstanceState: Bundle?) {
        mRootView = findViewById(R.id.rootview)
        mAppBarLayout = findViewById(R.id.appbarlayout)
        mToolbar = findViewById(R.id.toolbar)
        mTabLayout = findViewById(R.id.tablayout)
        if (mToolbar != null) {
            setSupportActionBar(mToolbar)
        }
    }

    protected fun setToolbarTitle(title: String) {
        supportActionBar?.title = title
    }

    protected fun setToolbarTitle(@StringRes stringId: Int) {
        supportActionBar?.setTitle(stringId)
    }

    protected fun setToolbarSubTitle(subTitle: String) {
        supportActionBar?.subtitle = subTitle
    }

    protected fun setToolbarSubTitle(@StringRes stringId: Int) {
        supportActionBar?.setSubtitle(stringId)
    }

    protected fun setToolbarBackEnable() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    protected fun setToolbarScrollFlags(@AppBarLayout.LayoutParams.ScrollFlags flags: Int) {
        val mParams: AppBarLayout.LayoutParams? = mAppBarLayout?.getChildAt(0)?.layoutParams as AppBarLayout.LayoutParams
        mParams?.scrollFlags = flags
    }

    protected fun setTabMode(@TabLayout.Mode mode: Int) {
        mTabLayout?.visibility = View.VISIBLE
        mTabLayout?.tabMode = mode
    }

    protected fun setupWithViewPager(@Nullable viewPager: ViewPager) {
        mTabLayout?.visibility = View.VISIBLE
        mTabLayout?.setupWithViewPager(viewPager)
    }

    protected fun showSnackbar(text: String) {
        if (mRootView != null) {
            Snackbar.make(mRootView!!, text, Snackbar.LENGTH_LONG).show()
        }
    }

    protected fun showSnackbar(@StringRes stringId: Int) {
        showSnackbar(getString(stringId))
    }

    protected fun showSnackbar(@StringRes btnTextId: String, @StringRes contentId: Int, click: View.OnClickListener) {
        if (mRootView != null) {
            Snackbar.make(mRootView!!, contentId, Snackbar.LENGTH_LONG)
                    .setAction(btnTextId, click)
                    .show()
        }
    }

    protected fun showProgress(@StringRes stringId: Int) {
        showProgress(getString(stringId))
    }

    protected fun showProgress(text: String) {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog(this)
            mProgressDialog?.setCancelable(false)
        }

        mProgressDialog?.setMessage(text)
        mProgressDialog?.show()
    }

    protected fun hideProgress() {
        mProgressDialog?.dismiss()
    }

    protected fun hideKeyboard() {
        val windowToken = currentFocus?.windowToken
        if (windowToken != null) {
            manager?.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    @LayoutRes
    abstract fun getContentView(): Int
}