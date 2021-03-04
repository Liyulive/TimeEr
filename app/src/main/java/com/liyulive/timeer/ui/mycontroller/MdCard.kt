package com.liyulive.timeer.ui.mycontroller

import android.content.res.Resources
import android.widget.ImageView
import com.google.android.material.card.MaterialCardView
import com.liyulive.timeer.R
import com.liyulive.timeer.TimeErApplication

object MdCard {

    /*一些杂七杂八复用的函数*/

    fun select(card: MaterialCardView) {

        card.strokeWidth =
            TimeErApplication.context.resources.getDimension(R.dimen.selected_dp).toInt()
        card.setStrokeColor(TimeErApplication.context.resources.getColor(R.color.black))
        card.cardElevation =
            TimeErApplication.context.resources.getDimension(R.dimen.selected_dp)

    }

    fun cancelSelect(card: MaterialCardView) {

        card.strokeWidth =
            TimeErApplication.context.resources.getDimension(R.dimen.default_dp).toInt()
        card.setStrokeColor(TimeErApplication.context.resources.getColor(R.color.stroke_default))
        card.cardElevation =
            TimeErApplication.context.resources.getDimension(R.dimen.default_dp)

    }

    fun getColor(resources: Resources, color: Int): Int {
        return when (color) {
            0 -> resources.getColor(R.color.type_0)
            1 -> resources.getColor(R.color.type_1)
            2 -> resources.getColor(R.color.type_2)
            3 -> resources.getColor(R.color.type_3)
            4 -> resources.getColor(R.color.type_4)
            5 -> resources.getColor(R.color.type_5)
            6 -> resources.getColor(R.color.type_6)
            7 -> resources.getColor(R.color.type_7)
            8 -> resources.getColor(R.color.type_8)
            9 -> resources.getColor(R.color.type_9)
            10 -> resources.getColor(R.color.type_10)
            11 -> resources.getColor(R.color.type_11)
            else -> resources.getColor(R.color.teal_700)
        }
    }

}