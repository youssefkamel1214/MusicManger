package com.example.musicapp.controller

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.models.Audio
import com.example.musicmanger.toMMSS
import com.example.musicmanger.databinding.MusicItemIconBinding

class MusicAdapter(val clickListener: MusicListener):RecyclerView.Adapter<MusicAdapter.ViewHolder>() {
    class ViewHolder(binding: MusicItemIconBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: MusicItemIconBinding
        init {
            this.binding = binding
        }
    }
    class MusicListener(val clickListener: (position: Int) -> Unit) {
        fun onClick(position: Int) = clickListener(position)
    }
    val musicController=MusicController.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding=MusicItemIconBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
          return musicController.audioList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val audio=musicController.audioList[position]
        holder.binding.timer.text=audio.duration.toMMSS()
        holder.binding.title.text=audio.Name
        holder.binding.constraint.setOnClickListener {
            clickListener.onClick(position)
        }
    }
}