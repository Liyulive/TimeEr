package com.liyulive.timeer.ui.mycontroller

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.liyulive.timeer.TimeErApplication
import com.liyulive.timeer.logic.model.ArcData
import com.liyulive.timeer.ui.home.HomeViewModel
import kotlinx.android.synthetic.main.fragment_dashboard.*

class ArcView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private lateinit var homeViewModel: HomeViewModel

    private var mHeight: Int = 0
    private var mWidth: Int = 0
    private var mPaint: Paint = Paint()

    private var centerX: Int = 0
    private var centerY: Int = 0

    /*数据*/
    var total: Double = 0.0
    lateinit var datas: ArrayList<ArcData>

    /*半径*/
    private var radius = 200

    init {
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.isAntiAlias = true
        datas = TimeErApplication.ArcData
    }

    fun setData(data: ArrayList<ArcData>) {
        datas = data
        total = 0.0
        for (i in 0 until datas.size) {
            total += datas[i].data
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        /*获取宽高*/
        mHeight = MeasureSpec.getSize(heightMeasureSpec)
        mWidth = MeasureSpec.getSize(widthMeasureSpec)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //无数据
//        if (datas.isEmpty()) return

        centerX = (right - left) / 2
        centerY = (bottom - top) / 2
        val min = if (mHeight > mWidth) {
            mWidth
        } else {
            mWidth
        }
        if (radius > min / 2) {
            radius = (min - paddingTop - paddingBottom / 3.5).toInt()
        }

        //画扇形
        canvas?.save()
        drawCircle(canvas)
        canvas?.restore()

    }

    private fun drawCircle(canvas: Canvas?) {
        val rect = RectF((centerX - radius).toFloat(),
            (centerY - radius).toFloat(), (centerX + radius).toFloat(),
            (centerY + radius).toFloat())

        var start = 0f
        //画Arc
        for (i in 0 until datas.size) {
            mPaint.color = MdCard.getColor(resources, datas[i].color)
            val angles: Float = ((datas[i].data.toFloat() / total.toFloat()) * 360).toFloat()
            canvas?.drawArc(rect, start, angles, true, mPaint)
            start += angles
        }
    }
}