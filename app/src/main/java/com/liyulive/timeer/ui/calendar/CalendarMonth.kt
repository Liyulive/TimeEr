package com.liyulive.timeer.ui.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.MonthView
import com.liyulive.timeer.R

class CalendarMonth(context: Context?) : MonthView(context) {
    private var mRadius = 0
    override fun onPreviewHook() {
        mRadius = Math.min(mItemWidth, mItemHeight) / 5 * 2
    }

    @SuppressLint("ResourceAsColor")
    override fun onDrawSelected(canvas: Canvas, calendar: Calendar, x: Int, y: Int, hasScheme: Boolean): Boolean {
        val cx = x + mItemWidth / 2
        val cy = y + mItemHeight / 2
        mSelectedPaint.color = 0xffdda5ae.toInt()
        canvas.drawCircle(cx.toFloat(), cy.toFloat(), mRadius.toFloat(), mSelectedPaint)
        return true
    }

    override fun onDrawScheme(canvas: Canvas, calendar: Calendar, x: Int, y: Int) {
        val cx = x + mItemWidth / 2
        val cy = y + mItemHeight / 2

        canvas.drawCircle(cx.toFloat(), cy.toFloat(), mRadius.toFloat(), mSchemePaint)
    }

    override fun onDrawText(canvas: Canvas, calendar: Calendar, x: Int, y: Int, hasScheme: Boolean, isSelected: Boolean) {
        val cx = x + mItemWidth / 2
        val top = y - mItemHeight / 8
        if (isSelected) {
            mSelectTextPaint.color = 0xffffffff.toInt()
            mSelectedLunarTextPaint.color = 0xffffffff.toInt()
            mCurDayTextPaint.color = 0xffffffff.toInt()
            mCurDayLunarTextPaint.color = 0xffffffff.toInt()
            canvas.drawText(calendar.day.toString(), cx.toFloat(), mTextBaseLine + top,
                if (calendar.isCurrentDay) mCurDayTextPaint else mSelectTextPaint)
            canvas.drawText(calendar.lunar, cx.toFloat(), mTextBaseLine + y + mItemHeight / 10,
                if (calendar.isCurrentDay) mCurDayLunarTextPaint else mSelectedLunarTextPaint)
        } else if (hasScheme) {
            mCurDayTextPaint.color = 0xffbf360c.toInt()
            mCurDayLunarTextPaint.color = 0xffbf360c.toInt()
            canvas.drawText(calendar.day.toString(), cx.toFloat(), mTextBaseLine + top,
                if (calendar.isCurrentDay) mCurDayTextPaint else if (calendar.isCurrentMonth) mSchemeTextPaint else mOtherMonthTextPaint)
            canvas.drawText(calendar.lunar, cx.toFloat(), mTextBaseLine + y + mItemHeight / 10, mSchemeLunarTextPaint)
        } else {
            mCurDayTextPaint.color = 0xffbf360c.toInt()
            mCurDayLunarTextPaint.color = 0xffbf360c.toInt()
            canvas.drawText(calendar.day.toString(), cx.toFloat(), mTextBaseLine + top,
                if (calendar.isCurrentDay) mCurDayTextPaint else if (calendar.isCurrentMonth) mCurMonthTextPaint else mOtherMonthTextPaint)
            canvas.drawText(calendar.lunar, cx.toFloat(), mTextBaseLine + y + mItemHeight / 10,
                if (calendar.isCurrentDay) mCurDayLunarTextPaint else mCurMonthLunarTextPaint)
        }
    }
}