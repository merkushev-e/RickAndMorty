package com.testtask.rickandmorty.presentation.locations.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.testtask.rickandmorty.databinding.EpisoderItemRvBinding
import com.testtask.rickandmorty.databinding.LocationItemRvBinding
import com.testtask.rickandmorty.domain.model.EpisodeData
import com.testtask.rickandmorty.domain.model.LocationData
import com.testtask.rickandmorty.presentation.episodes.adapter.EpisodesAdapter

class LocationsAdapter :
    PagingDataAdapter<LocationData, LocationsAdapter.LocationsViewHolder>(DiffUtilCallBack()) {


    var listener: OnListItemClickListener? = null

    override fun onBindViewHolder(holder: LocationsViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return LocationsViewHolder(
            LocationItemRvBinding.inflate(inflater, parent, false)
        )
    }

    inner class LocationsViewHolder(private val binding: LocationItemRvBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LocationData) {
            with(binding) {
                locationName.text = item.name
                locationType.text = item.type
                locationDimension.text = item.dimension
                locationItem.setOnClickListener {
                    listener?.onClick(item)
                }
            }
        }

    }

    fun interface OnListItemClickListener {
        fun onClick(item: LocationData)
    }
}


class DiffUtilCallBack : DiffUtil.ItemCallback<LocationData>() {
    override fun areItemsTheSame(oldItem: LocationData, newItem: LocationData): Boolean {
        return oldItem.id == newItem.id

    }

    override fun areContentsTheSame(oldItem: LocationData, newItem: LocationData): Boolean {
        return oldItem == newItem && oldItem.name == newItem.name
    }

}