package ru.antoncharov.waverecorder

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_files.*

class FilesActivity : AppCompatActivity() {
    private val mSetting : SharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_files)
    }

    override fun onResume() {
        super.onResume()
        val count = mSetting.getInt(COUNT, 0)
        val listOfFiles = arrayListOf<String?>()
        for (i in 1.. count){
            listOfFiles.add(mSetting.getString(i.toString(),null))
        }
        list_view.adapter = Adapter(listOfFiles)
    }
}
