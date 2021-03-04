package com.liyulive.timeer.ui.home

import android.os.Bundle
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
                ViewModelProvider(activity!!).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val layoutManager = LinearLayoutManager(activity)
        timeRecyclerView.layoutManager = layoutManager
        adapter = TimeListAdapter(this, homeViewModel.timeList)
        timeRecyclerView.adapter = adapter

        homeViewModel.timeLiveData.observe(viewLifecycleOwner, Observer { result ->
            homeViewModel.timeList.clear()
            homeViewModel.timeList.addAll(result)
            adapter.notifyDataSetChanged()
        })
        floatBtn.setOnClickListener {
            homeViewModel.getTimeList(homeViewModel.today)
            homeViewModel.lastTime = getLastTime(homeViewModel.timeList)
            val time = Timer(homeViewModel.today, homeViewModel.lastTime, System.currentTimeMillis(), -1, "空空如也~")
            val t = thread {
                TimeErDatabase.insertTimer(time)
            }
            t.join()
            homeViewModel.getTimeList(homeViewModel.today)
            adapter.notifyDataSetChanged()
            homeViewModel.lastTime = System.currentTimeMillis()
            timeRecyclerView.scrollToPosition(adapter.itemCount - 1)
        }
    }

    override fun onResume() {
        super.onResume()
        thread {
            homeViewModel.typeList = Repository.queryAllType() as ArrayList<DiyType>
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