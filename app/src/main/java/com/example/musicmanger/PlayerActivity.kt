package com.example.musicmanger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import com.example.musicapp.controller.MusicController
import com.example.musicmanger.databinding.ActivityPlayerBinding
import java.util.*

class PlayerActivity : AppCompatActivity() {
    private val binding: ActivityPlayerBinding by lazy {
        ActivityPlayerBinding.inflate(layoutInflater)
    }
    private val musicController: MusicController by lazy { MusicController.getInstance() }
    private  var  position:Int=-1
    val timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        position=intent.getIntExtra("position",-1)
        musicController.startMusic(position,this)
        setaudiodata()
        setclikcListeners()
        musicController.setOnCompletelisner {
            if (position < musicController.audioList.size - 1)
                position += 1
            else
                position = 0
            musicController.startMusic(position, this@PlayerActivity)
            setaudiodata()
        }
        timer.scheduleAtFixedRate(object :TimerTask(){
            override fun run() {
                updateaudiodata()
            }
        },1000,1000)
    }

    private fun updateaudiodata() {
        if(musicController.isplaying()) {
                runOnUiThread {
                    binding.seekBar.progress = musicController.getprogress()
                    binding.currenttime.text = musicController.getprogress().toLong().toMMSS()
                }
        }
    }

    private fun setclikcListeners() {
        binding.playpasue.setOnClickListener {
            if(musicController.isplaying()) {
                musicController.pause()
                binding.playpasue.setImageResource(R.drawable.baseline_play_circle_outline_24)
            }
            else{
                musicController.play()
                binding.playpasue.setImageResource(R.drawable.baseline_pause_circle_outline_24)
            }
        }
        binding.skipnext.setOnClickListener {
            if (position<musicController.audioList.size-1)
                position+=1
            else
                position=0
            musicController.startMusic(position,this@PlayerActivity)
            setaudiodata()

        }
        binding.skipprevios.setOnClickListener {
            if (position>0)
                position-=1
            else
                position=musicController.audioList.size-1
            musicController.startMusic(position,this@PlayerActivity)
            setaudiodata()
        }
        binding.stop.setOnClickListener { finish() }
    }

    private fun setaudiodata() {

        binding.title.text=musicController.audioList[position].Name
        binding.seekBar.max= musicController.audioList[position].duration.toInt()
        binding.duration.text=musicController.audioList[position].duration.toMMSS()
        binding.currenttime.text="00:00"
        binding.seekBar.progress=0
        binding.seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                musicController.seekto(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }


        })
    }

    override fun onPause() {
        super.onPause()
        musicController.pause()
    }

    override fun onStart() {
        super.onStart()

        if(!musicController.isplaying())
            musicController.play()
    }

    override fun onDestroy() {
        super.onDestroy()
        musicController.stop()
        timer.cancel()
    }
}