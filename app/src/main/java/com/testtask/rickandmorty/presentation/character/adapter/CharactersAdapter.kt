package com.testtask.rickandmorty.presentation.character.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.testtask.rickandmorty.R
import com.testtask.rickandmorty.databinding.CharactersItemRvBinding
import com.testtask.rickandmorty.domain.model.CharactersData

class CharactersAdapter() :
    PagingDataAdapter<CharactersData, CharactersAdapter.CharactersViewHolder>(DiffUtilCallBack()) {


    var listener: OnListItemClickListener? = null

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CharactersViewHolder(
            CharactersItemRvBinding.inflate(inflater, parent, false)
        )
    }

    inner class CharactersViewHolder(private val binding: CharactersItemRvBinding) :
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
        fun onClick(item : CharactersData)
    }
}


class DiffUtilCallBack : DiffUtil.ItemCallback<CharactersData>() {
    override fun areItemsTheSame(oldItem: CharactersData, newItem: CharactersData): Boolean {
        return oldItem.id == newItem.id

    }

    override fun areContentsTheSame(oldItem: CharactersData, newItem: CharactersData): Boolean {
        return oldItem == newItem && oldItem.name == newItem.name
    }

}