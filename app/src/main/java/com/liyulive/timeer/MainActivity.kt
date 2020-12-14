package com.liyulive.timeer

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AnimationUtils
import android.widget.CalendarView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.haibin.calendarview.Calendar
import com.liyulive.timeer.logic.TimerDB
import com.liyulive.timeer.logic.database.TimeErDatabase
import com.liyulive.timeer.logic.model.Timer
import com.liyulive.timeer.ui.home.HomeViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.SimpleDateFormat
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), com.haibin.calendarview.CalendarView.OnCalendarSelectListener{

    lateinit var homeViewModel: HomeViewModel

    private var isExpanded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolBar)
        appBar.setExpanded(false)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        /*生成动画*/
        val rotateDown = AnimationUtils.loadAnimation(this, R.anim.rotate_down)
        val rotateUp = AnimationUtils.loadAnimation(this, R.anim.rotate_up)
        rotateDown.fillAfter = true
        rotateUp.fillAfter = true
        rotateDown.interpolator = AccelerateInterpolator()
        rotateUp.interpolator = AccelerateInterpolator()

        toolText.setOnClickListener {
            if (isExpanded) {
                appBar.setExpanded(false)
                isExpanded = false
                toolbarArrow.startAnimation(rotateUp)
            } else {
                appBar.setExpanded(true)
                isExpanded = true
                toolbarArrow.startAnimation(rotateDown)
            }
        }

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_settings))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        calendar.setOnCalendarSelectListener(this)

        /*启动查询*/
        homeViewModel.today = "${calendar.curYear}-${calendar.curMonth}-${calendar.curDay}"
        homeViewModel.getTimeList(homeViewModel.today)

    }

    override fun onCalendarOutOfRange(calendar: Calendar?) {
        TODO("Not yet implemented")
    }

    @SuppressLint("SetTextI18n")
    override fun onCalendarSelect(calendar: Calendar?, isClick: Boolean) {
        toolBarDate.text = "${calendar?.year}年${calendar?.month}月${calendar?.day}日"
        toolBarWeek.text = calendar?.week?.let { weekToCn(it) }.toString()
        homeViewModel.getTimeList("${calendar?.year}-${calendar?.month}-${calendar?.day}")
        if (calendar?.isCurrentDay == true) {
            floatBtn.visibility = View.VISIBLE
        } else {
            floatBtn.visibility = View.GONE
        }
    }

    private fun weekToCn(week: Int): String {
        when (week) {
            1 -> return "星期一"
            2 -> return "星期二"
            3 -> return "星期三"
            4 -> return "星期四"
            5 -> return "星期五"
            6 -> return "星期六"
            0 -> return "星期日"
        }
        return "星期八"
    }

}