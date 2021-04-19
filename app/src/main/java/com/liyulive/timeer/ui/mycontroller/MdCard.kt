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

    fun getDimen(dp: Int) = when (dp) {
        0 -> TimeErApplication.context.resources.getDimension(R.dimen.dp_0)
        1 -> TimeErApplication.context.resources.getDimension(R.dimen.dp_1)
        2 -> TimeErApplication.context.resources.getDimension(R.dimen.dp_2)
        3 -> TimeErApplication.context.resources.getDimension(R.dimen.dp_3)
        4 -> TimeErApplication.context.resources.getDimension(R.dimen.dp_4)
        5 -> TimeErApplication.context.resources.getDimension(R.dimen.dp_5)
        6 -> TimeErApplication.context.resources.getDimension(R.dimen.dp_6)
        7 -> TimeErApplication.context.resources.getDimension(R.dimen.dp_7)
        8 -> TimeErApplication.context.resources.getDimension(R.dimen.dp_8)
        9 -> TimeErApplication.context.resources.getDimension(R.dimen.dp_9)
        10 -> TimeErApplication.context.resources.getDimension(R.dimen.dp_10)
        11 -> TimeErApplication.context.resources.getDimension(R.dimen.dp_11)
        12 -> TimeErApplication.context.resources.getDimension(R.dimen.dp_12)
        13 -> TimeErApplication.context.resources.getDimension(R.dimen.dp_13)
        14 -> TimeErApplication.context.resources.getDimension(R.dimen.dp_14)
        15 -> TimeErApplication.context.resources.getDimension(R.dimen.dp_15)
        16 -> TimeErApplication.context.resources.getDimension(R.dimen.dp_16)
        else -> TimeErApplication.context.resources.getDimension(R.dimen.default_dp)
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

    fun getArcColor(resources: Resources, color: Int): Int {
        return when (color) {
            0 -> resources.getColor(R.color.arc_0)
            1 -> resources.getColor(R.color.arc_1)
            2 -> resources.getColor(R.color.arc_2)
            3 -> resources.getColor(R.color.arc_3)
            4 -> resources.getColor(R.color.arc_4)
            5 -> resources.getColor(R.color.arc_5)
            6 -> resources.getColor(R.color.arc_6)
            7 -> resources.getColor(R.color.arc_7)
            8 -> resources.getColor(R.color.arc_8)
            9 -> resources.getColor(R.color.arc_9)
            10 -> resources.getColor(R.color.arc_10)
            11 -> resources.getColor(R.color.arc_11)
            else -> resources.getColor(R.color.teal_700)
        }
    }

}