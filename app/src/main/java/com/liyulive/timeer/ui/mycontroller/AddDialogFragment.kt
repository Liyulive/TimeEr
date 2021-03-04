package com.liyulive.timeer.ui.mycontroller

import android.os.Bundle
import android.view.*
import android.widget.Adapter
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.liyulive.timeer.R
import com.liyulive.timeer.TimeErApplication
import com.liyulive.timeer.logic.Repository
import com.liyulive.timeer.logic.model.DiyType
import com.liyulive.timeer.ui.adapter.ColorAdapter
import kotlinx.android.synthetic.main.dialog_add_type.*
import kotlinx.android.synthetic.main.edit_fragment.*
import kotlin.concurrent.thread

class AddDialogFragment(var Type: DiyType) : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.dialog_add_type, container, false)
    }

    override fun onStart() {
        super.onStart()

        //区分添加和保存
        if (tag == "addType") {
            edit_type_save.visibility = View.GONE
            add_type_save.visibility = View.VISIBLE
            textView_addDialog_label.text = "添加类型"
        } else if (tag == "editType") {
            edit_type_save.visibility = View.VISIBLE
            add_type_save.visibility = View.GONE
            textView_addDialog_label.text = "编辑类型"
            editText_add_type_title.setText(Type.typeName)
            editText_add_type_context.setText(Type.typeContext)
        }

        //设置颜色选择recyclerview
        val colorList = listOf<Int>(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)
        val layoutManager = LinearLayoutManager(TimeErApplication.context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        val adapter = ColorAdapter(colorList, resources, Type.typeColor)
        recyclerViewColor.layoutManager = layoutManager
        recyclerViewColor.adapter = adapter

        //以下必须在onStart中设置生效
        val lp = dialog?.window?.attributes
        val window = dialog?.window  //不设置导致与屏幕有距离
        lp?.gravity = Gravity.BOTTOM
        lp?.windowAnimations = R.style.dialog_bottom_full_anim
        lp?.width = resources.displayMetrics.widthPixels  //设置宽度，左右填满
        lp?.height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.attributes = lp

        add_type_save.setOnClickListener {
            val mType = DiyType(editText_add_type_title.text.toString(), editText_add_type_context.text.toString(), adapter.selectColor())
            thread {
                Repository.addType(mType)
            }
            dismiss()
        }

        edit_type_save.setOnClickListener {
            Type.typeName = editText_add_type_title.text.toString()
            Type.typeContext = editText_add_type_context.text.toString()
            Type.typeColor = adapter.selectColor()
            thread {
                Repository.updateType(Type)
            }
            dismiss()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}