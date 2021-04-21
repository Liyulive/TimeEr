package com.liyulive.timeer.ui.settings

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.OpenableColumns
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toFile
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.liyulive.timeer.R
import com.liyulive.timeer.TimeErApplication
import com.liyulive.timeer.logic.Repository
import com.liyulive.timeer.logic.model.DiyType
import com.liyulive.timeer.logic.model.Timer
import com.liyulive.timeer.ui.adapter.TimeListAdapter
import kotlinx.android.synthetic.main.activity_import_and_export.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.*
import java.lang.reflect.Type
import kotlin.concurrent.thread
import kotlin.math.min
import kotlin.random.Random

class ImportAndExportActivity : AppCompatActivity() {

    private val mTag = 11
    val version = Build.VERSION.SDK_INT

    @SuppressLint("SdCardPath")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_import_and_export)
        setSupportActionBar(import_toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        val sharedPreferences = this.getSharedPreferences("TimeErData", 0)

        card_export.setOnClickListener {

            val timeList = Repository.queryAllTime()
            val typeList = Repository.queryAllType()

            val gson = Gson()
            val allTime: String = gson.toJson(timeList)
            val allType: String = gson.toJson(typeList)
            sharedPreferences.edit().putString("timeList", allTime).apply()
            sharedPreferences.edit().putString("typeList", allType).apply()

            writeTo(
                readXml(File("data/data/com.liyulive.timeer/shared_prefs/TimeErData.xml")),
                getDownDirs(),
                "timeer.tdb"
            )
            Handler().postDelayed({
                writeTo(
                    readXml(File("data/data/com.liyulive.timeer/shared_prefs/TimeErData.xml")),
                    getDownDirs(),
                    "timeer.tdb"
                )
            }, 100)

            Toast.makeText(this, "数据已保存到Download/timeer.tdb", Toast.LENGTH_SHORT).show()
        }

        card_import.setOnClickListener {
            openFileManager()
        }
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data != null) {
            if (requestCode == mTag) {
                var file: File? = null
                val uri: Uri? = data.data
                if (uri != null) {

                    file = if (version < 29) {
                        uriToFileN(this, uri)
                    } else {
                        uriToFileQ(this, uri)
                    }

                    if (file == null) {
                        Toast.makeText(TimeErApplication.context, "没有此文件", Toast.LENGTH_SHORT)
                            .show()
                    }
                    writeTo(
                        readXml(file),
                        File("data/data/com.liyulive.timeer/shared_prefs/"),
                        "importData.xml"
                    )

                    val importSharedPreferences = this.getSharedPreferences("importData", 0)
                    val allTime = importSharedPreferences.getString("timeList", "")
                    val allType = importSharedPreferences.getString("typeList", "")
                    if (allTime == "" && allType == "") {
                        Toast.makeText(TimeErApplication.context, "该文件非备份文件", Toast.LENGTH_SHORT)
                            .show()
                        return
                    } else {
                        val gson = Gson()
                        val typeOfTime: Type = object : TypeToken<ArrayList<Timer>>() {} .type
                        val typeOfType: Type = object : TypeToken<ArrayList<DiyType>>() {}.type
                        val timeList: ArrayList<Timer> = gson.fromJson(allTime, typeOfTime)
                        val typeList: ArrayList<DiyType> = gson.fromJson(allType, typeOfType)
                        thread {
                            Repository.deleteAllTimer()
                            Repository.deleteAllType()
                            timeList.forEach {
                                Repository.typeDao.insertTimer(it)
                            }
                            typeList.forEach {
                                Repository.addType(it)
                            }
                        }
                        Toast.makeText(TimeErApplication.context, "导入完成", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    /*兼容安卓Q的uri转file，因沙盒机制*/
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun uriToFileQ(context: Context, uri: Uri): File? =
        if (uri.scheme == ContentResolver.SCHEME_FILE)
            uri.toFile()
        else if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            //把文件保存到沙盒
            val contentResolver = context.contentResolver
            val cursor = contentResolver.query(uri, null, null, null, null)
            cursor?.let {
                if (it.moveToFirst()) {
                    val ois = context.contentResolver.openInputStream(uri)
                    val displayName =
                        it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    ois?.let {
                        File(
                            context.externalCacheDir!!.absolutePath,
                            "${Random.nextInt(0, 9999)}$displayName"
                        ).apply {
                            val fos = FileOutputStream(this)
                            android.os.FileUtils.copy(ois, fos)
                            fos.close()
                            it.close()
                        }
                    }
                } else null

            }

        } else null

    fun uriToFileN(context: Context, uri: Uri): File? {
        try {
            val returnCursor = context.contentResolver.query(uri, null, null, null, null)
            val nameIndex: Int = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            val name: String = returnCursor.getString(nameIndex)
            val file = File(context.filesDir, name)
            val inputStream = context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            var read = 0
            val maxBufferSize = 1 * 1024 * 1024
            val bytesAvailable = inputStream?.available()
            val bufferSize = bytesAvailable?.let { min(it, maxBufferSize) }
            val buffers = bufferSize?.let { ByteArray(it) }
            while ((inputStream?.read(buffers).also { read = it!! }) != -1) {
                outputStream.write(buffers, 0, read)
            }
            returnCursor.close()
            inputStream?.close()
            outputStream.close()
            return file
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    /*获取Download目录对象*/
    fun getDownDirs(): File {
        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }

    /*写入*/
    fun writeTo(str: String, dictionary: File, fileName: String) {
        val strContent = "$str"
        var file: File? = null
        try {
            file = File(dictionary, fileName)
            if (!file.exists()) {
                file.createNewFile()
            } else {
                val fileWriter = FileWriter(file)
                fileWriter.write("")
                fileWriter.flush()
                fileWriter.close()
            }
            val raf = RandomAccessFile(file, "rwd")
            raf.seek(file.length())
            raf.write(strContent.toByteArray())
            raf.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /*读取shared_prefs*/
    fun readXml(file: File?): String {
        var xmlString = ""

        try {
            val inputStream =
                FileInputStream(file)
            val length = inputStream.available()
            val buffer = ByteArray(length)
            inputStream.read(buffer)
            xmlString = String(buffer)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return xmlString
    }

    fun openFileManager() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("*/*")
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(intent, mTag)
    }

}