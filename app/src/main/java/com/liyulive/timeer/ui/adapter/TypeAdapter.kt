package com.liyulive.timeer.ui.adapter

import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import com.liyulive.timeer.R
import com.liyulive.timeer.TimeErApplication
import com.liyulive.timeer.logic.Repository
import com.liyulive.timeer.logic.Repository.queryAllType
import com.liyulive.timeer.logic.model.DiyType
import com.liyulive.timeer.ui.mycontroller.AddDialogFragment
import com.liyulive.timeer.ui.mycontroller.MdCard
import kotlin.concurrent.thread

class TypeAdapter(var typeList: ArrayList<DiyType>, val resources: Resources, val fragmentManager: FragmentManager) : RecyclerView.Adapter<TypeAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val type_title: TextView = view.findViewById(R.id.textView_DIYType)
        val type_context: TextView = view.findViewById(R.id.textView_DIYType_context)
        val type_card: MaterialCardView = view.findViewById(R.id.card_DIYType)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_type, parent, false)
        val holder = ViewHolder(view)

        holder.type_card.setOnLongClickListener {
            //TODO 一堆bug
            val mType = typeList[holder.adapterPosition].id.toInt() - 1
            val deleteList = Repository.queryTimerFromType(mType)
            deleteList.forEach {
                it.type = -1
                Repository.updateTimeItem(it)
            }

            //删除后查询显示
            val bakType = typeList[holder.adapterPosition]
            val bakPosition = holder.adapterPosition
            Repository.deleteType(bakType)
            typeList.clear()
            typeList.addAll(queryAllType() as ArrayList<DiyType>)
//            notifyDataSetChanged()
            notifyItemRemoved(holder.adapterPosition)
            Snackbar.make(view, "删除成功", Snackbar.LENGTH_SHORT).setAction("撤销") {
                Repository.addType(bakType)
                deleteList.forEach {
                    it.type = mType
                    Repository.updateTimeItem(it)
                }
                typeList.clear()
                typeList.addAll(queryAllType() as ArrayList<DiyType>)
//                notifyDataSetChanged()
                notifyItemInserted(bakPosition)
                Toast.makeText(TimeErApplication.context, "已撤销", Toast.LENGTH_SHORT).show()
            }.show()

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
            if (!addDialogFragment.isAdded) {
                addDialogFragment.setOnButtonClickListener(object :
                    AddDialogFragment.DialogOnClickListener {
                    override fun addClick() {
                    }

                    override fun editClick() {
                        typeList.clear()
                        typeList.addAll(queryAllType() as ArrayList<DiyType>)
//                    notifyDataSetChanged()
                        notifyItemChanged(position)
                    }
                })
                addDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog_bottom_full)
                addDialogFragment.show(fragmentManager, "editType")
            }
        }

    }

    override fun getItemCount() = typeList.size

}