package com.testtask.rickandmorty.presentation.character

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.testtask.rickandmorty.App
import com.testtask.rickandmorty.R
import com.testtask.rickandmorty.databinding.FragmentCharactersBinding
import com.testtask.rickandmorty.domain.AppState
import com.testtask.rickandmorty.presentation.character.adapter.CharactersAdapter
import com.testtask.rickandmorty.presentation.character.adapter.CharactersLoadStateAdapter
import com.testtask.rickandmorty.presentation.character.viewModel.CharactersViewModel
import com.testtask.rickandmorty.utils.simpleScan
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


        val tryAgainAction = { adapter.retry() }
        val footerAdapter = CharactersLoadStateAdapter(tryAgainAction)
        val adapterLoadState = adapter.withLoadStateFooter(footerAdapter)
        charactersLoadStateHolder = CharactersLoadStateAdapter.ViewHolder(
            binding.loadStateView,
            binding.swipeRefreshLayout,
            tryAgainAction
        )

        with(binding) {
            charactersRV.adapter = adapterLoadState
            charactersRV.layoutManager = GridLayoutManager(requireContext(), 2).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
                    override fun getSpanSize(position: Int): Int {
                        Log.d("TAGG",adapter.itemCount.toString())
                        return if (adapter.itemCount - 10  == position-2) {
                            Log.d("TAGG", "position in $position")
                            return 2
                        } else{
                            return 1
                        }
                    }
                }
            }
            adapter.listener = CharactersAdapter.OnListItemClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, CharacterDetailsFragment.newInstance(Bundle().apply {
                        putParcelable(CharacterDetailsFragment.CHARACTER_EXTRA, it)
                    }))
                    .commit()
            }
        }

        getData()
        setupSwipeToRefresh()
        observeLoadState(adapter)
        handleListVisibility(adapter)
    }


    private fun getData() {
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
                binding.charactersRV.isInvisible = current is LoadState.Error
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
            is AppState.Success -> {
                showViewSuccess(appState)
                appState.data
            }
            is AppState.Error -> {

                showErrorScreen(appState.error.message)
            }
            else -> {}
        }

    }

    private fun showErrorScreen(message: String?) {
        Toast.makeText(requireActivity(),"Cannot load data", Toast.LENGTH_LONG).show()
    }


    private fun showViewSuccess(appState: AppState.Success) {
        viewLifecycleOwner.lifecycleScope.launch {
            appState.data?.let { adapter.submitData(it) }
        }

    }

    private fun setupSwipeToRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
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
