package com.liyulive.timeer.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.liyulive.timeer.R
import com.liyulive.timeer.ui.settings.diyType.DiyTypeActivity
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        settingsViewModel =
                ViewModelProvider(this).get(SettingsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_settings, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setting_diy_type.setOnClickListener {
            val intent = Intent(this.activity, DiyTypeActivity::class.java)
            startActivity(intent)
        }

        setting_ImportAndExport.setOnClickListener {
            val intent = Intent(this.activity, ImportAndExportActivity::class.java)
            startActivity(intent)
        }

        setting_about.setOnClickListener {
            val intent = Intent(this.activity, AboutActivity::class.java)
            startActivity(intent)
        }

        setting_general.setOnClickListener {
            val intent = Intent(this.activity, GeneralSettingActivity::class.java)
            startActivity(intent)
        }
    }
}