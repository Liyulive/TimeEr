package com.liyulive.timeer.ui.home

import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.liyulive.timeer.R
import com.liyulive.timeer.TimeErApplication
import com.liyulive.timeer.logic.Repository
import com.liyulive.timeer.logic.database.TimeErDatabase
import com.liyulive.timeer.logic.model.ArcData
import com.liyulive.timeer.logic.model.DiyType
import com.liyulive.timeer.logic.model.Timer
import com.liyulive.timeer.ui.adapter.TimeListAdapter
import com.liyulive.timeer.ui.mycontroller.ArcView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var adapter: TimeListAdapter
    private var lastTime = 0L //get last time
    var changeList = ArrayList<Timer>() //用于自定义数据Timer->ArcData

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        homeViewModel.timeLiveData.observe(viewLifecycleOwner, Observer { result ->
            homeViewModel.timeList =
                Repository.queryTimeByDate(homeViewModel.selectDay) as ArrayList<Timer>
            homeViewModel.timeListForAdapter.clear()
            homeViewModel.timeListForAdapter.addAll(homeViewModel.timeList)

            /*为空显示背景*/
            if (homeViewModel.timeList.isEmpty()) {
                timeRecyclerView.visibility = View.INVISIBLE
                layout_nullItem.visibility = View.VISIBLE
                textView_nullItem.text = "什么也没有呢，从今天开始记录吧"
            } else {
                timeRecyclerView.visibility = View.VISIBLE
                layout_nullItem.visibility = View.INVISIBLE
                textView_nullItem.text = "什么也没有呢，点击按钮来记录今天的数据吧"
            }

            /*获取统计图的数据*/
            TimeErApplication.ArcData.clear()
            changeList.clear()
            homeViewModel.timeListForAdapter.parallelStream()
                .collect(Collectors.groupingBy({ o -> o.type }, Collectors.toList()))
                .forEach { id, transfer ->
                    transfer.stream()
                        .reduce { a, b ->
                            Timer(
                                a.date,
                                a.startTime,
                                a.endTime,
                                a.type,
                                a.context,
                                a.haveTime + b.haveTime
                            )
                        }
                        .ifPresent {
                            changeList.add(it)
                        }
                }
            changeList.forEach {
                if (homeViewModel.typeList.isNotEmpty()) {
                    if (it.type != -1) {
                        val type = it.type
                        TimeErApplication.ArcData.add(
                            ArcData(
                                it.haveTime.toDouble(),
                                it.id.toInt(),
                                homeViewModel.typeList.filter { it.id.toInt() == type + 1 }[0].typeName
                            )
                        )
                    } else {
                        TimeErApplication.ArcData.add(
                            ArcData(
                                it.haveTime.toDouble(),
                                it.id.toInt(),
                                "未定义类型"
                            )
                        )
                    }
                }
            }

            /*非今天不可添加数据*/
            if (homeViewModel.selectDay == homeViewModel.today) {
                floatBtn?.visibility = View.VISIBLE
            } else {
                floatBtn?.visibility = View.GONE
            }

            //notifyDataSetChanged必须保证list没变，用add不能用=
            adapter.notifyDataSetChanged()
        })
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val fragment = this
        homeViewModel.typeList = Repository.queryAllType() as ArrayList<DiyType>
        val layoutManager = LinearLayoutManager(activity)
        timeRecyclerView.layoutManager = layoutManager
        adapter = TimeListAdapter(fragment, homeViewModel.timeListForAdapter, homeViewModel.typeList)
        timeRecyclerView.adapter = adapter


        if (homeViewModel.firstRun) {
            val sharedPreferences = activity?.getSharedPreferences("FirstRun", 0)
            Handler().postDelayed({
                homeViewModel.typeList = Repository.queryAllType() as ArrayList<DiyType>
                adapter = TimeListAdapter(fragment, homeViewModel.timeListForAdapter, homeViewModel.typeList)
                timeRecyclerView.adapter = adapter
            }, 100)
            sharedPreferences?.edit()?.putBoolean("First", false)?.apply()
            homeViewModel.firstRun = false
        }

        floatBtn.setOnClickListener {
            homeViewModel.getTimeList(homeViewModel.today)
            if (lastTime == 0L) {
                lastTime = getLastTime(homeViewModel.timeList)
            }
            val endTime = System.currentTimeMillis()
            val startTime = lastTime
            val time = Timer(homeViewModel.today,
                startTime,
                endTime,
                -1,
                "点击卡片来进行编辑吧",
                startTime - endTime)
            val t = thread {
                TimeErDatabase.insertTimer(time)
            }
            t.join()
            adapter.notifyItemInserted(adapter.itemCount)
            lastTime = System.currentTimeMillis()
            timeRecyclerView.scrollToPosition(adapter.itemCount)
        }
    }

    private fun getLastTime(list: List<Timer>): Long {
        var lastTime: Long = getMills(homeViewModel.today)
        list.forEach{
            if (it.endTime > lastTime) {
                lastTime = it.endTime
            }
        }
        return lastTime
    }

    private fun getMills(str: String): Long {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val date = simpleDateFormat.parse(str)
        val mills = date.time
        return mills
    }

}