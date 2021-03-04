package com.liyulive.timeer.ui.mycontroller

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.liyulive.timeer.R
import com.liyulive.timeer.TimeErApplication
import com.liyulive.timeer.logic.Repository
import com.liyulive.timeer.logic.model.DiyType
import com.liyulive.timeer.logic.model.Timer
import com.liyulive.timeer.ui.adapter.TypeSelectorAdapter
import kotlinx.android.synthetic.main.edit_fragment.*
import kotlin.concurrent.thread

class EditDialogFragment(var timer: Timer) : DialogFragment() {

    lateinit var typeList: ArrayList<DiyType>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.edit_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()

        //以下必须在onStart中设置生效
        val lp = dialog?.window?.attributes
        val window = dialog?.window  //不设置导致与屏幕有距离
        lp?.gravity = Gravity.BOTTOM
        lp?.windowAnimations = R.style.dialog_bottom_full_anim
        lp?.width = resources.displayMetrics.widthPixels  //设置宽度，左右填满
        lp?.height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.attributes = lp

        thread {
            typeList = Repository.queryAllType() as ArrayList<DiyType>
        }.join()

        val layoutManager = LinearLayoutManager(TimeErApplication.context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        val adapter = TypeSelectorAdapter(typeList, resources)
        recyclerViewTypeSelector.layoutManager = layoutManager
        recyclerViewTypeSelector.adapter = adapter
        adapter.setListener {
            edit_context.setText(it)
        }

        edit_save.setOnClickListener {
            timer.type = adapter.mPosition
            timer.context = edit_context.text.toString()
            thread {
                Repository.updateTimeItem(timer)
            }.join()
            adapter.notifyDataSetChanged()
            dismiss()
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}