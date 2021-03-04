package com.liyulive.timeer.ui.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.liyulive.timeer.R
import com.liyulive.timeer.logic.model.DiyType
import com.liyulive.timeer.ui.mycontroller.MdCard

class TypeSelectorAdapter(val typeList: List<DiyType>, val resources: Resources) :
    RecyclerView.Adapter<TypeSelectorAdapter.ViewHolder>() {

    var mPosition: Int = -1

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card: MaterialCardView = view.findViewById(R.id.card_type_selector)
        val textType: TextView = view.findViewById(R.id.textView_type_selector)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TypeSelectorAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_type_selector, parent, false)
        val holder = ViewHolder(view)
        holder.card.setOnClickListener {

        }
        return holder
    }

    override fun onBindViewHolder(holder: TypeSelectorAdapter.ViewHolder, position: Int) {
        holder.textType.text = typeList[position].typeName
        holder.card.setCardBackgroundColor(MdCard.getColor(resources, typeList[position].typeColor))

        /*设置回调监听方法*/
        holder.card.setOnClickListener {
            mPosition = holder.adapterPosition
            MdCard.select(holder.card)
            notifyDataSetChanged()
            this.mListener(typeList[position].typeContext)
        }

        if (position == mPosition) {
            MdCard.select(holder.card)
        } else {
            MdCard.cancelSelect(holder.card)
        }
    }

    override fun getItemCount(): Int {
        return typeList.size
    }

    lateinit var mListener: (context: String) -> Unit

    fun setListener(listener: (String) -> Unit) {
        this.mListener = listener
    }

}