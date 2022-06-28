package com.testtask.rickandmorty.presentation.character.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.testtask.rickandmorty.databinding.EpisoderItemRvBinding

import com.testtask.rickandmorty.domain.model.EpisodeData

class CharactersDetailsAdapter :
    RecyclerView.Adapter<CharactersDetailsAdapter.CharacterDetailsEpisodesViewHolder>() {
    private var episodes: List<EpisodeData> = listOf()

    fun setData(data: List<EpisodeData>){
        episodes = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CharacterDetailsEpisodesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CharacterDetailsEpisodesViewHolder(EpisoderItemRvBinding.inflate(inflater,parent, false))
    }

    override fun onBindViewHolder(holder: CharacterDetailsEpisodesViewHolder, position: Int) {
        holder.bind(episodes[position])
    }

    override fun getItemCount(): Int {
        return episodes.size
    }


    inner class CharacterDetailsEpisodesViewHolder(private val binding: EpisoderItemRvBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: EpisodeData) {
            with(binding) {
                episodeName.text = item.episode
                episodeDate.text = item.air_date
                episodesPilot.text = item.name

            }
        }
    }


}