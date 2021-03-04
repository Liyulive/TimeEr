package com.liyulive.timeer.ui.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.liyulive.timeer.R
import com.liyulive.timeer.TimeErApplication
import com.liyulive.timeer.logic.Repository
import com.liyulive.timeer.logic.model.DiyType
import com.liyulive.timeer.ui.mycontroller.AddDialogFragment
import com.liyulive.timeer.ui.mycontroller.MdCard
import kotlin.concurrent.thread

class TypeAdapter(var typeList: MutableList<DiyType>, val resources: Resources, val fragmentManager: FragmentManager) : RecyclerView.Adapter<TypeAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val type_title: TextView = view.findViewById(R.id.textView_DIYType)
        val type_context: TextView = view.findViewById(R.id.textView_DIYType_context)
        val type_card: MaterialCardView = view.findViewById(R.id.card_DIYType)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_type, parent, false)
        val holder = ViewHolder(view)

        holder.type_card.setOnLongClickListener {
            Repository.deleteType(typeList[holder.adapterPosition])
            Toast.makeText(TimeErApplication.context, "删除成功", Toast.LENGTH_SHORT).show()
            thread {
                typeList.clear()
                typeList = Repository.queryAllType() as MutableList<DiyType>
            }
            notifyDataSetChanged()
            true
        }

        return holder
    }

    override fun onBindViewHolder(holder: TypeAdapter.ViewHolder, position: Int) {
        val DiyType = typeList[position]
        holder.type_title.text = DiyType.typeName
        holder.type_context.text = DiyType.typeContext
        holder.type_card.setCardBackgroundColor(MdCard.getColor(resources, DiyType.typeColor))

        holder.type_card.setOnClickListener {
            val addDialogFragment = AddDialogFragment(DiyType)
            addDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog_bottom_full)
            addDialogFragment.show(fragmentManager, "editType")
        }
    }

    override fun getItemCount() = typeList.size

}