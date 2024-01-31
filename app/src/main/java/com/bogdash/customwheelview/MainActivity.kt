package com.bogdash.customwheelview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import com.bogdash.customwheelview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.wheelView.setOnRotationEndListener(binding.imageView, binding.tvColor)
        binding.button.setOnClickListener {
            binding.wheelView.reset()
            binding.tvColor.text = getString(R.string.touch)
            binding.seekBar.progress = 50
        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val scale = progress / 50f
                binding.wheelView.scaleX = scale
                binding.wheelView.scaleY = scale
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

    }
}