package ru.antoncharov.waverecorder

import android.media.AudioFormat
import android.media.AudioRecord
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get


class PreferencesActivity : AppCompatActivity() {
    var settings = arrayListOf<Int>()
    var list = arrayListOf<ArrayList<Int>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)
        val rates = intArrayOf(8000, 11025, 22050, 44100, 48000, 96000)
        val chanels = intArrayOf(AudioFormat.CHANNEL_IN_MONO, AudioFormat.CHANNEL_IN_STEREO)
        val encs =  intArrayOf(AudioFormat.ENCODING_PCM_8BIT, AudioFormat.ENCODING_PCM_16BIT)
        val constraint : ConstraintLayout = findViewById<ConstraintLayout>(R.id.preferences_layout)
        val radioGroup = RadioGroup(this)
        radioGroup.orientation = RadioGroup.VERTICAL

        for (enc in encs) {
            for (ch in chanels) {
                for (rate in rates) {
                    val t = AudioRecord.getMinBufferSize(rate, ch, enc)
                    if (t != AudioRecord.ERROR && t != AudioRecord.ERROR_BAD_VALUE) {
                        settings.add(enc)
                        settings.add(ch)
                        settings.add(rate)
                        list.add(settings)
                        val radioButton = RadioButton(this)
                        radioButton.text = "$enc / $ch / $rate"
                        radioGroup.addView(radioButton)
                    }
                }
            }
        }

        constraint.addView(radioGroup)

        if(list.size != 0) {
            Preferences.settings = list[0]
            (radioGroup[0] as RadioButton).isChecked = true
        }
        radioGroup.setOnCheckedChangeListener { radioGroup, i ->  Preferences.settings = list[i]}
    }
}
