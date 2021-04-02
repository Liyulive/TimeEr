package com.liyulive.timeer.ui.settings.diyType

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.liyulive.timeer.R
import com.liyulive.timeer.TimeErApplication
import com.liyulive.timeer.logic.Repository
import com.liyulive.timeer.logic.model.DiyType
import com.liyulive.timeer.ui.adapter.TypeAdapter
import com.liyulive.timeer.ui.mycontroller.AddDialogFragment
import kotlinx.android.synthetic.main.activity_diy_type.*
import kotlin.concurrent.thread

class DiyTypeActivity : AppCompatActivity() {

    private var TypeList = ArrayList<DiyType>()
    lateinit var adapter: TypeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diy_type)
        setSupportActionBar(diy_toolbar)

        /*设置返回键*/
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        /*test*/
        //val type = DiyType("学习", "吃饭睡觉打豆豆", 2)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.diy_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_type -> {
                val nullType = DiyType("", "", 0)
                val addDialogFragment = AddDialogFragment(nullType)
                addDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog_bottom_full)
                addDialogFragment.setOnButtonClickListener(object :
                    AddDialogFragment.DialogOnClickListener {
                    override fun addClick() {
                        TypeList.clear()
                        TypeList.addAll(Repository.queryAllType() as ArrayList<DiyType>)
                        adapter.notifyDataSetChanged()
                    }

                    override fun editClick() {
                    }
                })
                addDialogFragment.show(this.supportFragmentManager, "addType")
            }
            android.R.id.home -> finish()
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        TypeList.clear()
        thread {
            TypeList = Repository.queryAllType() as ArrayList<DiyType>
        }.join()
        val layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        adapter = TypeAdapter(TypeList, resources, this.supportFragmentManager)
        recyclerViewType.layoutManager = layoutManager
        recyclerViewType.adapter = adapter
        Log.d("DiyTypeActivity", "onResume")
    }
}