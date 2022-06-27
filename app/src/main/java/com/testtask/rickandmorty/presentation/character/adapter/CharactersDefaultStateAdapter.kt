package com.testtask.rickandmorty.presentation.character.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.testtask.rickandmorty.databinding.CharactersItemRvBinding
import com.testtask.rickandmorty.databinding.LoadStateMessaageBinding


class CharactersLoadStateAdapter(private val tryAgainAction: () -> Unit): LoadStateAdapter<CharactersLoadStateAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LoadStateMessaageBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, null, tryAgainAction)
    }

    class ViewHolder(
        private val binding: LoadStateMessaageBinding,
        private val swipeRefreshLayout: SwipeRefreshLayout?,
        private val tryAgainAction: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {


        init {
            binding.tryAgainButton.setOnClickListener { tryAgainAction() }
        }

        fun bind(loadState: LoadState) = with(binding) {
            messageTextView.isVisible = loadState is LoadState.Error
            tryAgainButton.isVisible = loadState is LoadState.Error
            if (swipeRefreshLayout != null) {
                swipeRefreshLayout.isRefreshing = loadState is LoadState.Loading
                progressBar.isVisible = false
            } else {
                progressBar.isVisible = loadState is LoadState.Loading
            }
        }
    }


}
