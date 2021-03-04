package com.liyulive.timeer.ui.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.liyulive.timeer.R
import com.liyulive.timeer.ui.mycontroller.MdCard

class ColorAdapter(val colorList: List<Int>, val resources: Resources, val selectedPosition: Int) : RecyclerView.Adapter<ColorAdapter.ViewHolder>() {

    var mPosition: Int = selectedPosition

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card: MaterialCardView = view.findViewById(R.id.card_color)
        val check: ImageView = view.findViewById(R.id.image_select)
    }

    fun selectColor(): Int {
        return mPosition
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_color, parent, false)
        val holder = ViewHolder(view)
        holder.card.setOnClickListener {
            mPosition = holder.adapterPosition
            holder.check.visibility = View.VISIBLE
            notifyDataSetChanged()
        }
        return holder
    }

    override fun onBindViewHolder(holder: ColorAdapter.ViewHolder, position: Int) {
        holder.card.setCardBackgroundColor(MdCard.getColor(resources, colorList[position]))

        if (position == mPosition) {
            holder.check.visibility = View.VISIBLE
        } else {
            holder.check.visibility = View.INVISIBLE
        }
    }

    override fun getItemCount() = colorList.size

}