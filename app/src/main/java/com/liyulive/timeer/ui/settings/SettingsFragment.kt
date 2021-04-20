package com.liyulive.timeer.ui.settings

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.liyulive.timeer.MainActivity
import com.liyulive.timeer.R
import com.liyulive.timeer.TimeErApplication
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

            XXPermissions.with(this).permission(Permission.MANAGE_EXTERNAL_STORAGE).request(object :
                OnPermissionCallback {

                override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                    if (all) {
                        startActivity(intent)
                    }
                }

                override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                    if (never) {
                        Toast.makeText(
                            TimeErApplication.context,
                            "授权被拒绝，请手动授予存储权限",
                            Toast.LENGTH_SHORT
                        ).show()
                        XXPermissions.startPermissionActivity(this@SettingsFragment, permissions)
                    } else {
                        Toast.makeText(TimeErApplication.context, "获取权限失败", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            })
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