package com.liyulive.timeer.ui.dashboard

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.liyulive.timeer.R
import com.liyulive.timeer.TimeErApplication
import com.liyulive.timeer.logic.Repository
import com.liyulive.timeer.logic.model.ArcData
import com.liyulive.timeer.logic.model.Timer
import com.liyulive.timeer.ui.adapter.ArcTypeAdapter
import com.liyulive.timeer.ui.home.HomeViewModel
import kotlinx.android.synthetic.main.fragment_dashboard.*
import java.util.stream.Collectors

class DashboardFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        return root
    }

    override fun onResume() {
        super.onResume()
        val adapter = ArcTypeAdapter(TimeErApplication.ArcData, resources)
        val layoutManager = LinearLayoutManager(this.context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerViewArc?.layoutManager = layoutManager
        recyclerViewArc?.adapter = adapter
        arc.setData(TimeErApplication.ArcData)
        arc.invalidate()
    }
}