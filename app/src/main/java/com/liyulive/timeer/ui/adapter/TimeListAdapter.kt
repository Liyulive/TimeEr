package com.liyulive.timeer.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.liyulive.timeer.R
import com.liyulive.timeer.logic.model.Timer
import java.text.SimpleDateFormat

class TimeListAdapter(private val fragment: Fragment, val timeList: List<Timer>) :
    RecyclerView.Adapter<TimeListAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timeText: TextView = view.findViewById(R.id.item_time_text)
        val timeContent: TextView = view.findViewById(R.id.item_time_context)
        val card: MaterialCardView = view.findViewById(R.id.type_card)
        val type: TextView = view.findViewById(R.id.type_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.time_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val simpleDateFormat = SimpleDateFormat("yyyy-M-d /HH:mm")
        val todayMills: Long = getTodayMills(timeList[position].date)
        val haveTime: Long = timeList[position].endTime - timeList[position].startTime
        val startTime = simpleDateFormat.format(timeList[position].startTime)
        val startTimeIndex = startTime.lastIndexOf("/")
        val endTime = simpleDateFormat.format(timeList[position].endTime)
        val endTimeIndex = endTime.lastIndexOf("/")
        holder.timeText.text = "${startTime.substring(startTimeIndex + 1)}-${endTime.substring(endTimeIndex + 1)} 共${millsToHM(haveTime)}"
        holder.timeContent.text = timeList[position].context
    }

    override fun getItemCount() = timeList.size

    private fun getTodayMills(str: String): Long { /*日期转毫秒*/
        val simpleDateFormat = SimpleDateFormat("yyyy-M-d")
        val date = simpleDateFormat.parse(str)
        val mills = date.time
        return mills
    }

    fun millsToHM(mss: Long): String? { /*毫秒到小时分*/
        val hours = mss % (1000 * 60 * 60 * 24) / (1000 * 60 * 60)
        val minutes = mss % (1000 * 60 * 60) / (1000 * 60)
        return "${hours}时${minutes}分"
    }
}