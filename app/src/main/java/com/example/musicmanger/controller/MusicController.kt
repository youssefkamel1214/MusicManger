package com.example.musicapp.controller

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.provider.MediaStore
import com.example.musicapp.models.Audio

class MusicController {
    fun startMusic(position: Int, baseContext: Context) {
        musicPlayer.reset()
        musicPlayer.setDataSource(baseContext, Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,audioList[position].Id.toString()))
        musicPlayer.prepare()
        musicPlayer.start()
    }

    fun seekto(progress: Int) {
        musicPlayer.seekTo(progress)
    }

    fun isplaying(): Boolean {
             return musicPlayer.isPlaying
    }

    fun play() {
        musicPlayer.start()
    }

    fun pause() {
        musicPlayer.pause()
    }

    fun setOnCompletelisner(onCompletionListener: MediaPlayer.OnCompletionListener) {
        if(musicPlayer.isPlaying)
            musicPlayer.setOnCompletionListener(onCompletionListener)
    }

    fun stop() {
        if(musicPlayer.isPlaying)
            musicPlayer.stop()

    }


    companion object{
        private var instance: MusicController?=null
        fun getInstance(): MusicController {
            if (instance ==null)
                instance = MusicController()
            return instance!!
        }
    }
    protected fun finalize() {
        // cleanup code here
        audioList.clear()
        if(musicPlayer.isPlaying)
            musicPlayer.stop()
        musicPlayer.release()
    }

    fun getprogress(): Int {
       return musicPlayer.currentPosition
    }

    val musicPlayer=MediaPlayer()
    val audioList=ArrayList<Audio>()
}