package com.example.musicmanger

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicapp.controller.MusicAdapter
import com.example.musicapp.controller.MusicController
import com.example.musicapp.models.Audio
import com.example.musicmanger.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val MainActivityTag="MainActivity"
    private val REQUEST_Audio_Permission=1001
    private val musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    private val musicController : MusicController by lazy {
        MusicController.getInstance()
    }
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val musicAdapter : MusicAdapter by lazy {
        MusicAdapter(MusicAdapter.MusicListener {
            val intent = Intent(this@MainActivity, PlayerActivity::class.java)
            intent.putExtra("position",it)
            startActivity(intent)
        })
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        checkPremissionAndGetAudios()
        binding.recyclerview.layoutManager= LinearLayoutManager(this)
        binding.recyclerview.adapter=musicAdapter
    }

    private fun checkPremissionAndGetAudios() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES
                ) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.READ_MEDIA_IMAGES,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.FOREGROUND_SERVICE

                    ), REQUEST_Audio_Permission
                );

            }
            else {
                getallaudios()
            }
        }
        else{
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,

                        ), REQUEST_Audio_Permission
                    )
                }
                else{
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,

                            ), REQUEST_Audio_Permission
                    )
                }

            }
            else {
                getallaudios()
            }
        }
    }

    private fun getallaudios() {
        val selection = "${MediaStore.Audio.Media.DATA} like ? "
        val selectionArgs = arrayOf("%/storage/emulated/0/%")
        val cursor =contentResolver.query(musicUri,null,selection,selectionArgs,null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
                val name=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
                val duration=cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
                musicController.audioList.add(Audio(id,name,duration))
                Log.d(MainActivityTag,"this music name:$name and id is $id")
            }
            if(musicController.audioList.isNotEmpty()){
                musicAdapter.notifyDataSetChanged()
            }
            cursor.close()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==REQUEST_Audio_Permission){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getallaudios()
            }
            else{
                Toast.makeText(this,"cant load audios", Toast.LENGTH_LONG).show()
            }
        }
    }
}