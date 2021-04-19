package com.liyulive.timeer.ui.adapter

import android.annotation.SuppressLint
import android.view.*
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.liyulive.timeer.R
import com.liyulive.timeer.TimeErApplication
import com.liyulive.timeer.logic.model.DiyType
import com.liyulive.timeer.logic.model.Timer
import com.liyulive.timeer.ui.home.HomeFragment
import com.liyulive.timeer.ui.home.HomeViewModel
import com.liyulive.timeer.ui.mycontroller.EditDialogFragment
import com.liyulive.timeer.ui.mycontroller.MdCard
import java.text.SimpleDateFormat
import java.util.*

class TimeListAdapter(
    private val fragment: HomeFragment,
    val timeList: List<Timer>,
    val typeList: List<DiyType>
) :
        RecyclerView.Adapter<TimeListAdapter.ViewHolder>() {

    private lateinit var homeViewModel: HomeViewModel
    val sharedPreferences = fragment.activity?.getSharedPreferences("GeneralSetting", 0)
    //private lateinit var typeList: ArrayList<DiyType>

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timeText: TextView = view.findViewById(R.id.item_time_text)
        val timeContent: TextView = view.findViewById(R.id.item_time_context)
        val card: MaterialCardView = view.findViewById(R.id.time_card)
        val typeCard: MaterialCardView = view.findViewById(R.id.type_card)
        val type: TextView = view.findViewById(R.id.type_text)
        val haveTimeCard: MaterialCardView = view.findViewById(R.id.card_have_time)
        val haveTimeText: TextView = view.findViewById(R.id.item_have_time_text)
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
        val id = timeList[position].type + 1

        /*card属性设置*/
        val cardRadius: Int = sharedPreferences?.getInt("card_radius", 8) ?: 8
        val cardElevation: Boolean = sharedPreferences?.getBoolean("card_elevation", false) ?: false
        if (cardElevation) {
            holder.card.cardElevation = MdCard.getDimen(2)
        } else {
            holder.card.cardElevation = MdCard.getDimen(0)
        }
        holder.card.radius = MdCard.getDimen(cardRadius)

        if (timeList[position].type == -1) {
            holder.type.text = "未定义类型"
            holder.typeCard.setCardBackgroundColor(fragment.resources.getColor(R.color.flt))
            holder.haveTimeCard.setCardBackgroundColor(fragment.resources.getColor(R.color.flt))
        } else {
            holder.type.text = typeList.filter { it.id.toInt() == id }[0].typeName
            holder.typeCard.setCardBackgroundColor(
                MdCard.getColor(
                    fragment.resources,
                    typeList.filter { it.id.toInt() == id }[0].typeColor
                )
            )
            holder.haveTimeCard.setCardBackgroundColor(
                MdCard.getColor(
                    fragment.resources,
                    typeList.filter { it.id.toInt() == id }[0].typeColor
                )
            )
        }
        holder.timeText.text = "${startTime.substring(startTimeIndex + 1)}-${endTime.substring(endTimeIndex + 1)}"
        holder.haveTimeText.text = millsToHM(haveTime)
        holder.timeContent.text = timeList[position].context

        val editDialogFragment = EditDialogFragment(timeList[position])
        holder.card.setOnClickListener {
            editDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog_bottom_full)
            editDialogFragment.show(fragment.parentFragmentManager, "yes")
        }
    }

    override fun getItemCount() = timeList.size

    private fun getTodayMills(str: String): Long { /*日期转毫秒*/
        val simpleDateFormat = SimpleDateFormat("yyyy-M-d")
        val date = simpleDateFormat.parse(str)
        val mills = date.time
        return mills
    }

    private fun millsToHM(mss: Long): String { /*毫秒到小时分*/
        val hours = mss % (1000 * 60 * 60 * 24) / (1000 * 60 * 60)
        val minutes = mss % (1000 * 60 * 60) / (1000 * 60)
        if (hours == 0L) return "${minutes}分"
        return "${hours}时${minutes}分"
    }

}