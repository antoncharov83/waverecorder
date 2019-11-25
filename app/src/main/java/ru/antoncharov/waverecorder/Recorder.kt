package ru.antoncharov.waverecorder

import android.media.MediaRecorder
import android.os.Build
import java.io.IOException

class Recorder {

    var output: String? = null
    var filename: String? = null
    private var mediaRecorder: MediaRecorder? = MediaRecorder()
    var state: Boolean = false
    var recordingStopped: Boolean = false

    constructor(path : String?) {
        output = path
//        mediaRecorder = MediaRecorder()

//        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
//        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
//        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
    }

    fun startRecording() : String?{
        try {
            mediaRecorder = MediaRecorder()

            if(Preferences.settings != null){
                mediaRecorder?.setAudioSamplingRate(Preferences.settings[0])
                mediaRecorder?.setAudioChannels(Preferences.settings[1])
                mediaRecorder?.setAudioEncodingBitRate(Preferences.settings[2])
            }

            mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            mediaRecorder?.setOutputFile(output + filename)
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            state = true
        } catch (e: IllegalStateException) {
            return e.message
        } catch (e: IOException) {
            return e.message
        }
        return "Запись началась"
    }

    private fun resumeRecording() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mediaRecorder?.resume()
        }
        recordingStopped = false
    }

    fun pauseRecording(){
        if (state) {
            if (!recordingStopped) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    mediaRecorder?.pause()
                    recordingStopped = true
                }
            } else {
                resumeRecording()
            }
        }
    }

    fun stopRecording() : String?{
        mediaRecorder?.stop()
        mediaRecorder?.reset()
        mediaRecorder?.release()
        state = false
        return "Запись остановлена. $output$filename"
    }

}