package com.liyulive.timeer.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.liyulive.timeer.R
import com.liyulive.timeer.TimeErApplication
import com.liyulive.timeer.logic.Repository
import com.liyulive.timeer.logic.database.TimeErDatabase
import com.liyulive.timeer.logic.model.DiyType
import com.liyulive.timeer.logic.model.Timer
import com.liyulive.timeer.ui.adapter.TimeListAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.SimpleDateFormat
import kotlin.concurrent.thread

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var adapter: TimeListAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

//        adapter.notifyDataSetChanged()
        //homeViewModel.timeListForAdapter = Repository.queryTimeByDate(homeViewModel.selectDay) as ArrayList<Timer>
        homeViewModel.timeLiveData.observe(viewLifecycleOwner, Observer { result ->
//            homeViewModel.timeList.clear()
//            homeViewModel.timeList.addAll(result)
            homeViewModel.timeList =
                Repository.queryTimeByDate(homeViewModel.selectDay) as ArrayList<Timer>
            homeViewModel.timeListForAdapter.clear()
            homeViewModel.timeListForAdapter.addAll(homeViewModel.timeList)
            Log.d("HomeFragment", "observe")
//            homeViewModel.timeListForAdapter = Repository.queryTimeByDate(homeViewModel.today) as ArrayList<Timer>
//            homeViewModel.timeListForAdapter = homeViewModel.timeList
            adapter.notifyDataSetChanged()
            //notifyDataSetChanged必须保证list没变，用add不能用=
        })
//        homeViewModel.forAdapterLiveData.observe(viewLifecycleOwner) {
//            adapter.notifyDataSetChanged()
//        }
        homeViewModel.typeList = Repository.queryAllType() as ArrayList<DiyType>
//        homeViewModel.timeListForAdapter.clear()
//        homeViewModel.timeListForAdapter.addAll(Repository.queryTimeByDate(homeViewModel.selectDay) as ArrayList<Timer>)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val layoutManager = LinearLayoutManager(activity)
        timeRecyclerView.layoutManager = layoutManager
        adapter = TimeListAdapter(this, homeViewModel.timeListForAdapter, homeViewModel.typeList)
        timeRecyclerView.adapter = adapter

//        homeViewModel.typeListLiveData.observe(viewLifecycleOwner) {
//            adapter.notifyDataSetChanged()
//        }

        floatBtn.setOnClickListener {
            homeViewModel.getTimeList(homeViewModel.today)
            homeViewModel.lastTime = getLastTime(homeViewModel.timeList)
            val time = Timer(homeViewModel.today, homeViewModel.lastTime, System.currentTimeMillis(), -1, "点击卡片来进行编辑吧")
            val t = thread {
                TimeErDatabase.insertTimer(time)
            }
            t.join()
            adapter.notifyItemInserted(adapter.itemCount)
            homeViewModel.lastTime = System.currentTimeMillis()
            timeRecyclerView.scrollToPosition(adapter.itemCount)
        }
    }

    override fun onResume() {
        super.onResume()
        /*thread {
            homeViewModel.timeListForAdapter = Repository.queryAllTime() as ArrayList<Timer>
            homeViewModel.typeList = Repository.queryAllType() as ArrayList<DiyType>
        }.join()*/
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