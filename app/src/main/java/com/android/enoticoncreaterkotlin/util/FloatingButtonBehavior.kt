package com.android.enoticoncreaterkotlin.util

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorListener
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FloatingButtonBehavior : FloatingActionButton.Behavior {

    private val interpolator = FastOutSlowInInterpolator()
    private var mIsAnimatingOut = false

    constructor() : super()
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)


    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, directTargetChild: View, target: View, axes: Int, type: Int): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
                || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type)
    }

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)
        if (dyConsumed > 0 && !mIsAnimatingOut && child.visibility == View.VISIBLE) {
            hide(child)
        } else if (dyConsumed < 0 && child.visibility != View.VISIBLE) {
            show(child)
        }
    }

    private fun hide(button: FloatingActionButton) {
        ViewCompat.animate(button)
                .scaleX(0.0f)
                .scaleY(0.0f)
                .alpha(0.0f)
                .setInterpolator(interpolator)
                .withLayer()
                .setListener(object : ViewPropertyAnimatorListener {
                    override fun onAnimationStart(view: View) {
                        mIsAnimatingOut = true
                    }

                    override fun onAnimationCancel(view: View) {
                        mIsAnimatingOut = false
                    }

                    override fun onAnimationEnd(view: View) {
                        mIsAnimatingOut = false
                        view.visibility = View.INVISIBLE
                    }
                })
                .start()
    }

    private fun show(button: View) {
        button.visibility = View.VISIBLE
        ViewCompat.animate(button)
                .scaleX(1.0f)
                .scaleY(1.0f)
                .alpha(1.0f)
                .setInterpolator(interpolator)
                .withLayer()
                .setListener(null)
                .start()
    }

}