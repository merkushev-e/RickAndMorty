package com.testtask.rickandmorty.presentation.episodes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.testtask.rickandmorty.databinding.EpisoderItemRvBinding
import com.testtask.rickandmorty.domain.model.EpisodeData

class EpisodesAdapter :
    PagingDataAdapter<EpisodeData, EpisodesAdapter.EpisodesViewHolder>(DiffUtilCallBack()) {


    var listener: OnListItemClickListener? = null

    override fun onBindViewHolder(holder: EpisodesViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return EpisodesViewHolder(
            EpisoderItemRvBinding.inflate(inflater, parent, false)
        )
    }

    inner class EpisodesViewHolder(private val binding: EpisoderItemRvBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: EpisodeData) {
            with(binding) {
                episodeName.text = item.episode
                episodeDate.text = item.air_date
                episodesPilot.text = item.name
            }
        }


    }

    fun interface OnListItemClickListener {
        fun onClick(item: EpisodeData)
    }
}


class DiffUtilCallBack : DiffUtil.ItemCallback<EpisodeData>() {
    override fun areItemsTheSame(oldItem: EpisodeData, newItem: EpisodeData): Boolean {
        return oldItem.id == newItem.id

    }

    override fun areContentsTheSame(oldItem: EpisodeData, newItem: EpisodeData): Boolean {
        return oldItem == newItem && oldItem.name == newItem.name
    }

}