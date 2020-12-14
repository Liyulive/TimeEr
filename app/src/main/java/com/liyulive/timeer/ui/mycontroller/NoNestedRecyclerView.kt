package com.liyulive.timeer.systemui.MyController

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView


class NoNestedRecyclerView : RecyclerView {
    var isNestedEnable = false

    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, @Nullable attrs: AttributeSet?) : super(context!!, attrs) {}
    constructor(context: Context?, @Nullable attrs: AttributeSet?, defStyle: Int) : super(context!!, attrs, defStyle) {}

    override fun startNestedScroll(axes: Int, type: Int): Boolean {
        return if (isNestedEnable) {
            super.startNestedScroll(axes, type)
        } else {
            false
        }
    }

    override fun stopNestedScroll(type: Int) {
        if (isNestedEnable) {
            super.stopNestedScroll(type)
        }
    }

    override fun hasNestedScrollingParent(type: Int): Boolean {
        return if (isNestedEnable) {
            super.hasNestedScrollingParent(type)
        } else {
            false
        }
    }

    override fun dispatchNestedScroll(dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, offsetInWindow: IntArray?, type: Int): Boolean {
        return if (isNestedEnable) {
            super.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type)
        } else {
            false
        }
    }

    override fun dispatchNestedPreScroll(dx: Int, dy: Int, consumed: IntArray?, offsetInWindow: IntArray?, type: Int): Boolean {
        return if (isNestedEnable) {
            super.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type)
        } else {
            false
        }
    }
}