package com.liyulive.timeer.ui.settings

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.SeekBar
import android.widget.Toast
import com.liyulive.timeer.R
import com.liyulive.timeer.TimeErApplication
import kotlinx.android.synthetic.main.activity_general_setting.*

class GeneralSettingActivity : AppCompatActivity() {



    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_general_setting)
        setSupportActionBar(general_toolbar)

        val sharedPreferences = this.getSharedPreferences("GeneralSetting", 0)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        switch_card_elevation.isChecked = sharedPreferences.getBoolean("card_elevation", false)
        switch_auto_close.isChecked = sharedPreferences.getBoolean("auto_close", true)
        textView_card_radius.text = sharedPreferences.getInt("card_radius", 8).toString() + " dp"
        seekBar_card_radius.progress = sharedPreferences.getInt("card_radius", 8)

        //SeekBar选择
        seekBar_card_radius.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                textView_card_radius.text = "$progress dp"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar?.progress?.let { sharedPreferences.edit().putInt("card_radius", it).apply() }
            }
        })

        switch_auto_close.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                sharedPreferences.edit().putBoolean("auto_close", true).apply()
            } else {
                sharedPreferences.edit().putBoolean("auto_close", false).apply()
            }
        }

        switch_card_elevation.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                sharedPreferences.edit().putBoolean("card_elevation", true).apply()
            } else {
                sharedPreferences.edit().putBoolean("card_elevation", false).apply()
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

}