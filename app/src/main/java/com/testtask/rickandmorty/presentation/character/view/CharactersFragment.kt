package com.testtask.rickandmorty.presentation.character.view


import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isInvisible
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
import com.testtask.rickandmorty.presentation.MainActivity.Companion.DETAILS_FRAGMENTS
import com.testtask.rickandmorty.presentation.character.adapter.CharactersAdapter
import com.testtask.rickandmorty.presentation.character.adapter.CharactersLoadStateAdapter
import com.testtask.rickandmorty.presentation.character.viewModel.CharactersViewModel
import com.testtask.rickandmorty.utils.OnlineLiveData
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
    private lateinit var onlineLiveData: OnlineLiveData
    private var isOnline: Boolean = false


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

        isOnline = isNetworkAvailable()
        if (isOnline){
            getData(true)
        } else{
            getData(false)
        }

        setupSwipeToRefresh()
        initNetworkObserver()

        initAdapter()
        observeLoadState(adapter)
        handleListVisibility(adapter)

    }

    private fun initNetworkObserver() {
        onlineLiveData = OnlineLiveData(requireActivity())
        onlineLiveData.observe(viewLifecycleOwner) { isOnline ->
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
    }


    private fun initAdapter() {

        val tryAgainAction = {
            isOnline = isNetworkAvailable()
            if (isOnline){
                getData(true)
            } else{
                getData(false)
            }
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
                    }), DETAILS_FRAGMENTS)
                    .addToBackStack("")
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

    private fun setupSwipeToRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            isOnline = isNetworkAvailable()
            if (!isOnline){
                Toast.makeText(
                    requireContext(),
                    getString(R.string.saved_data_showed),
                    Toast.LENGTH_LONG
                )
                    .show()
            }
            getData(isOnline)
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

    private fun isNetworkAvailable() : Boolean{
        val cm = requireActivity().getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
        return(capabilities !=null && capabilities.hasCapability(NET_CAPABILITY_INTERNET))
    }
}
