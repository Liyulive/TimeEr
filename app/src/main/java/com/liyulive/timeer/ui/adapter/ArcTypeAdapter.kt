package com.liyulive.timeer.ui.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.liyulive.timeer.R
import com.liyulive.timeer.logic.model.ArcData
import com.liyulive.timeer.ui.mycontroller.MdCard

class ArcTypeAdapter(private val ArcTypeList: List<ArcData>, val resources: Resources) :
    RecyclerView.Adapter<ArcTypeAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ArcColor: MaterialCardView = view.findViewById(R.id.card_ArcColor)
        val ArcName: TextView = view.findViewById(R.id.textView_ArcType)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_arc_context, parent, false)
        val holder = ViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.ArcColor.setCardBackgroundColor(MdCard.getColor(resources, ArcTypeList[position].color))
        holder.ArcName.text = ArcTypeList[position].name
    }

    override fun getItemCount() = ArcTypeList.size
}