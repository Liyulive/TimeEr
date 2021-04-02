package com.liyulive.timeer.ui.mycontroller

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.liyulive.timeer.R
import com.liyulive.timeer.TimeErApplication
import com.liyulive.timeer.logic.Repository
import com.liyulive.timeer.logic.model.DiyType
import com.liyulive.timeer.logic.model.Timer
import com.liyulive.timeer.ui.adapter.TypeSelectorAdapter
import com.liyulive.timeer.ui.home.HomeViewModel
import kotlinx.android.synthetic.main.edit_fragment.*
import kotlin.concurrent.thread

class EditDialogFragment(var timer: Timer) : DialogFragment() {

    lateinit var typeList: ArrayList<DiyType>
    lateinit var homeViewModel: HomeViewModel

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

        homeViewModel =
            ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)

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
            timer.type = typeList[adapter.mPosition].id.toInt() - 1
            if (timer.type == -1) {
                Toast.makeText(TimeErApplication.context, "请选择类型", Toast.LENGTH_SHORT).show()
            } else {
                timer.context = edit_context.text.toString()
                thread {
                    Repository.updateTimeItem(timer)
                }.join()
                homeViewModel.getTimeList(homeViewModel.selectDay)
                dismiss()
            }

        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}