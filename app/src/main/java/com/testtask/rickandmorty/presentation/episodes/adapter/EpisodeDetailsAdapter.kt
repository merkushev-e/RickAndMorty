package com.testtask.rickandmorty.presentation.episodes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.testtask.rickandmorty.R
import com.testtask.rickandmorty.databinding.CharactersItemRvBinding
import com.testtask.rickandmorty.domain.model.CharactersData
import com.testtask.rickandmorty.domain.model.EpisodeData

class EpisodeDetailsAdapter : RecyclerView.Adapter<EpisodeDetailsAdapter.EpisodeDetailsCharactersViewHolder>() {
    private var episodes: List<CharactersData> = listOf()

    var listener:OnListItemClickListener? = null

    fun setData(data: List<CharactersData>){
        episodes = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EpisodeDetailsCharactersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return EpisodeDetailsCharactersViewHolder(CharactersItemRvBinding.inflate(inflater,parent, false))
    }

    override fun onBindViewHolder(holder: EpisodeDetailsCharactersViewHolder, position: Int) {
        holder.bind(episodes[position])
    }

    override fun getItemCount(): Int {
        return episodes.size
    }



    inner class EpisodeDetailsCharactersViewHolder(private val binding: CharactersItemRvBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CharactersData) {
            with(binding) {
                characterName.text =  item.name
                characterStatus.text =  binding.root.context.getString(R.string.status) +  item.status
                characterSpecies.text = binding.root.context.getString(R.string.species) + item.species
                characterGender.text = binding.root.context.getString(R.string.gender) + item.gender
                characterImage.load(item.image){
                    error(R.drawable.ic_load_error_vector)
                    placeholder(R.drawable.ic_no_photo_vector)
                }
                charactersItem.setOnClickListener {
                    listener?.onClick(item)
                }
            }
        }
    }

    fun interface OnListItemClickListener {
        fun onClick(item: CharactersData)
    }

}
