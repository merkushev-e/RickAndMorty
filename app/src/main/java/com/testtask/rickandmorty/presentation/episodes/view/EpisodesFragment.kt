package com.testtask.rickandmorty.presentation.episodes.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.testtask.rickandmorty.App
import com.testtask.rickandmorty.R
import com.testtask.rickandmorty.databinding.FragmentEpisodesBinding
import com.testtask.rickandmorty.domain.AppState
import com.testtask.rickandmorty.domain.model.EpisodeData
import com.testtask.rickandmorty.presentation.character.adapter.CharactersLoadStateAdapter
import com.testtask.rickandmorty.presentation.episodes.viewmodel.EpisodesViewModel
import com.testtask.rickandmorty.presentation.episodes.adapter.EpisodesAdapter
import com.testtask.rickandmorty.utils.OnlineLiveData
import com.testtask.rickandmorty.utils.simpleScan
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class EpisodesFragment : Fragment() {

    private var _binding: FragmentEpisodesBinding? = null
    private val binding get() = _binding!!

    private val adapter: EpisodesAdapter by lazy {
        EpisodesAdapter()
    }
    private lateinit var charactersLoadStateHolder: CharactersLoadStateAdapter.ViewHolder
    private lateinit var viewModel: EpisodesViewModel
    private lateinit var onlineLiveData: OnlineLiveData


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelFactory = App.instance.component.getViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory)[EpisodesViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEpisodesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startLoadingOrShowError(false)
        onlineLiveData = OnlineLiveData(requireActivity())
        onlineLiveData.observe(viewLifecycleOwner) {
            startLoadingOrShowError(it)
            setupSwipeToRefresh(it)
            if (!it) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.saved_data_showed),
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        }

        initAdapter()
        observeLoadState(adapter)
        handleListVisibility(adapter)
    }

    private fun initAdapter() {

        val tryAgainAction = {
            adapter.refresh()
        }
        val footerAdapter = CharactersLoadStateAdapter(tryAgainAction)
        val adapterLoadState = adapter.withLoadStateFooter(footerAdapter)
        charactersLoadStateHolder = CharactersLoadStateAdapter.ViewHolder(
            binding.loadStateView,
            binding.swipeRefreshLayout,
            tryAgainAction
        )

        with(binding) {
            episodesRv.adapter = adapterLoadState
            episodesRv.layoutManager = GridLayoutManager(requireContext(), 2)
            adapter.listener = EpisodesAdapter.OnListItemClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, EpisodeDetailFragment.newInstance(Bundle().apply {
                        putParcelable(EpisodeDetailFragment.EPISODE_EXTRA, it)
                    }))
                    .commit()
            }
        }

    }


    private fun getData(isOnline: Boolean) {
        viewModel.liveData.observe(viewLifecycleOwner) { appState ->
            renderData(appState)
        }
        viewModel.getData(isOnline)
    }


    private fun observeLoadState(adapter: EpisodesAdapter) {
        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow.debounce(200).collectLatest { state ->
                charactersLoadStateHolder.bind(state.refresh)
            }
        }
    }

    private fun handleListVisibility(adapter: EpisodesAdapter) = lifecycleScope.launch {
        getRefreshLoadStateFlow(adapter)
            .simpleScan(count = 3)
            .collectLatest { (beforePrevious, previous, current) ->
                binding.episodesRv.isInvisible = current is LoadState.Error
                        || previous is LoadState.Error
                        || (beforePrevious is LoadState.Error && previous is LoadState.NotLoading
                        && current is LoadState.Loading)
            }
    }

    private fun getRefreshLoadStateFlow(adapter: EpisodesAdapter): Flow<LoadState> {
        return adapter.loadStateFlow.map { it.refresh }
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success<*> -> {
                showViewSuccess(appState as AppState.Success<PagingData<EpisodeData>>)
                appState.data
            }
            is AppState.Error -> {
                showErrorScreen(appState.error.message)
            }

            is AppState.Loading -> {
                showLoading()
            }
            else -> {}
        }

    }



    private fun startLoadingOrShowError(isOnline: Boolean) {
        if (isOnline) {
            getData(isOnline)
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.saved_data_showed),
                Toast.LENGTH_LONG
            )
                .show()
            getData(isOnline)
        }
    }


    private fun showErrorScreen(message: String?) {
        binding.loadStateView.messageTextView.text = message
    }


    private fun showViewSuccess(appState: AppState.Success<PagingData<EpisodeData>>) {
        viewLifecycleOwner.lifecycleScope.launch {
            appState.data?.let { adapter.submitData(it) }
        }

    }
    private fun showLoading() {
        binding.loadStateView.progressBar.visibility = View.VISIBLE
    }

    private fun setupSwipeToRefresh(isOnline: Boolean) {
        binding.swipeRefreshLayout.setOnRefreshListener {
            startLoadingOrShowError(isOnline)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        fun newInstance() = EpisodesFragment()
    }
}