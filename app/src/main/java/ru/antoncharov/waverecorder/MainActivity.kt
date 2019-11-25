package ru.antoncharov.waverecorder

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

const val APP_PREFERENCES = "wave_recorder_settings"
const val COUNT = "count"
const val RECORD_AUDIO_REQUEST_CODE =123

class MainActivity : AppCompatActivity() {
    private lateinit var recorder : Recorder
    private lateinit var mSetting : SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSetting = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        editor = mSetting.edit()

        try {
            requestPermissions()
            val file = this.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
            recorder = Recorder(file?.absolutePath)
        }catch (e:Exception){
            e.message
        }
        button_recording.setOnClickListener {
            if(!recorder.state){
                startRecording()
            }else{
                pauseRecording()
            }
        }

        button_stop_recording.setOnClickListener {
            if(recorder.state){
                stopRecording()
            }
        }

        button_files.setOnClickListener {
            val intent = Intent(this, FilesActivity::class.java)
            startActivity(intent)
        }

    }

    private fun checkPermissions() : Boolean{
        val codeRecord = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
        val codeSave = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return (codeRecord == PackageManager.PERMISSION_GRANTED) &&
                (codeSave == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermissions(){
        if (!checkPermissions()) {
            val permissions = arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            ActivityCompat.requestPermissions(this, permissions, 0)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == RECORD_AUDIO_REQUEST_CODE) {
            if (grantResults.size == 3 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED
                && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Права предоставлены", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Вы должны предоставить разрешения на запись и сохранение!", Toast.LENGTH_SHORT).show()
//                finishAffinity()
            }
        }
    }

    private fun startRecording(){
        requestPermissions()
        if(checkPermissions()) {
            if(Preferences.path != null)
                recorder.output = Preferences.path
            val sdf = SimpleDateFormat("dd/M/yyyy_hh:mm:ss")
            val currentDate = sdf.format(Date())
            recorder.filename = "/record_$currentDate.mp3"
            val message = recorder.startRecording()
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

            if (recorder.state) {
                button_recording.text = getString(R.string.record_btn_text_pause)
            }
        }
    }

    private fun pauseRecording(){
        recorder.pauseRecording()
        if(recorder.state) {
            if (recorder.recordingStopped) {
                button_recording.text = getString(R.string.record_btn_text_start)
            } else {
                button_recording.text = getString(R.string.record_btn_text_pause)
            }
        }
    }

    private fun stopRecording(){
        try {
            val message = recorder.stopRecording()
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            val count = mSetting.getInt(COUNT, 0) + 1
            editor.putString(count.toString(), recorder.output + recorder.filename)
            editor.putInt(COUNT, count)
        }catch (e:IllegalStateException){
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }

        button_recording.text = getString(R.string.record_btn_text_start)
        button_recording.setOnClickListener { startRecording() }
    }
}
