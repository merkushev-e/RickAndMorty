package com.testtask.rickandmorty.presentation.character.view

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.testtask.rickandmorty.App
import com.testtask.rickandmorty.R
import com.testtask.rickandmorty.databinding.FragmentCharactersBinding
import com.testtask.rickandmorty.domain.AppState
import com.testtask.rickandmorty.domain.model.CharactersData
import com.testtask.rickandmorty.presentation.character.adapter.CharactersAdapter
import com.testtask.rickandmorty.presentation.character.adapter.CharactersLoadStateAdapter
import com.testtask.rickandmorty.presentation.character.viewModel.CharactersViewModel
import com.testtask.rickandmorty.utils.OnlineLiveData
import com.testtask.rickandmorty.utils.simpleScan
import kotlinx.android.synthetic.main.fragment_episodes.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class CharactersFragment : Fragment() {


    private var _binding: FragmentCharactersBinding? = null
    private val binding get() = _binding!!
    private val adapter: CharactersAdapter by lazy {
        CharactersAdapter()
    }
    private lateinit var charactersLoadStateHolder: CharactersLoadStateAdapter.ViewHolder
    private lateinit var viewModel: CharactersViewModel
    private lateinit var onlineLiveData: OnlineLiveData


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelFactory = App.instance.component.getViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory)[CharactersViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCharactersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onlineLiveData = OnlineLiveData(requireActivity())
        Log.d("online", "from livedata")
        startLoadingOrShowError(false)
        onlineLiveData.observe(viewLifecycleOwner) {
            Log.d("online", "from livedata" + it.toString())
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
            charactersRV.adapter = adapterLoadState
            charactersRV.layoutManager = GridLayoutManager(requireContext(), 2)
            adapter.listener = CharactersAdapter.OnListItemClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, CharacterDetailsFragment.newInstance(Bundle().apply {
                        putParcelable(CharacterDetailsFragment.CHARACTER_EXTRA, it)
                    }))
                    .commit()
            }
        }

    }


    private fun getData(isOnline: Boolean) {
        viewModel.getData(isOnline)
        viewModel.liveData.observe(viewLifecycleOwner) { appState ->
            renderData(appState)
        }

    }


    private fun observeLoadState(adapter: CharactersAdapter) {
        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow.debounce(200).collectLatest { state ->
                charactersLoadStateHolder.bind(state.refresh)
            }
        }
    }

    private fun handleListVisibility(adapter: CharactersAdapter) = lifecycleScope.launch {
        getRefreshLoadStateFlow(adapter)
            .simpleScan(count = 3)
            .collectLatest { (beforePrevious, previous, current) ->
                binding.charactersRV.isInvisible =
                    current is LoadState.Error
                            || previous is LoadState.Error
                            || (beforePrevious is LoadState.Error && previous is LoadState.NotLoading
                            && current is LoadState.Loading)
            }

    }

    private fun getRefreshLoadStateFlow(adapter: CharactersAdapter): Flow<LoadState> {
        return adapter.loadStateFlow.map { it.refresh }
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success<*> -> {
                showViewSuccess(appState as AppState.Success<PagingData<CharactersData>>)
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
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showViewSuccess(appState: AppState.Success<PagingData<CharactersData>>) {
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
        fun newInstance() =
            CharactersFragment()
    }
}
