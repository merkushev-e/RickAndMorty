package com.testtask.rickandmorty.presentation.locations.view

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.testtask.rickandmorty.App
import com.testtask.rickandmorty.R
import com.testtask.rickandmorty.databinding.FragmentLocationsBinding
import com.testtask.rickandmorty.domain.AppState
import com.testtask.rickandmorty.domain.model.LocationData
import com.testtask.rickandmorty.presentation.MainActivity
import com.testtask.rickandmorty.presentation.character.adapter.CharactersLoadStateAdapter
import com.testtask.rickandmorty.presentation.locations.adapter.LocationsAdapter
import com.testtask.rickandmorty.presentation.locations.viewmodels.LocationsViewModel
import com.testtask.rickandmorty.utils.OnlineLiveData
import com.testtask.rickandmorty.utils.simpleScan
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class LocationsFragment : Fragment() {

    private var _binding: FragmentLocationsBinding? = null
    private val binding get() = _binding!!

    private val adapter: LocationsAdapter by lazy {
        LocationsAdapter()
    }

    private lateinit var viewModel: LocationsViewModel
    private lateinit var charactersLoadStateHolder: CharactersLoadStateAdapter.ViewHolder
    private lateinit var onlineLiveData: OnlineLiveData
    private var isOnline: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelFactory = App.instance.component.getViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory)[LocationsViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity?)!!.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as AppCompatActivity?)!!.supportActionBar?.setHomeButtonEnabled(false)
        setHasOptionsMenu(false);
        _binding = FragmentLocationsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        isOnline = isNetworkAvailable()
        if (isOnline) {
            getData(true)
        } else {
            getData(false)
        }

        setupSwipeToRefresh()
        initNetworkObserver()


        initAdapter()
        observeLoadState(adapter)
        handleListVisibility(adapter)
    }

    private fun initAdapter() {

        val tryAgainAction = {
            isOnline = isNetworkAvailable()
            if (isOnline) {
                getData(true)
            } else {
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
            locationRv.adapter = adapterLoadState
            locationRv.layoutManager = GridLayoutManager(requireContext(), 2)
            adapter.listener = LocationsAdapter.OnListItemClickListener {
                requireActivity().supportFragmentManager.beginTransaction()

                    .replace(R.id.container, LocationDetailsFragment.newInstance(Bundle().apply {
                        putParcelable(LocationDetailsFragment.Location_EXTRA, it)
                    }), MainActivity.DETAILS_FRAGMENTS)
                    .addToBackStack("")
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

    private fun setupSwipeToRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            isOnline = isNetworkAvailable()
            if (!isOnline) {
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


    private fun observeLoadState(adapter: LocationsAdapter) {
        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow.debounce(200).collectLatest { state ->
                charactersLoadStateHolder.bind(state.refresh)
            }
        }
    }

    private fun handleListVisibility(adapter: LocationsAdapter) = lifecycleScope.launch {
        getRefreshLoadStateFlow(adapter)
            .simpleScan(count = 3)
            .collectLatest { (beforePrevious, previous, current) ->
                binding.locationRv.isInvisible = current is LoadState.Error
                        || previous is LoadState.Error
                        || (beforePrevious is LoadState.Error && previous is LoadState.NotLoading
                        && current is LoadState.Loading)
            }
    }

    private fun getRefreshLoadStateFlow(adapter: LocationsAdapter): Flow<LoadState> {
        return adapter.loadStateFlow.map { it.refresh }
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success<*> -> {
                showViewSuccess(appState as AppState.Success<PagingData<LocationData>>)
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
        getData(isOnline)
    }

    private fun showErrorScreen(message: String?) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }


    private fun showViewSuccess(appState: AppState.Success<PagingData<LocationData>>) {
        viewLifecycleOwner.lifecycleScope.launch {
            appState.data?.let { adapter.submitData(it) }
        }

    }

    private fun setupSwipeToRefresh(isOnline: Boolean) {
        binding.swipeRefreshLayout.setOnRefreshListener {
            startLoadingOrShowError(isOnline)
        }
    }


    private fun showLoading() {
        binding.loadStateView.progressBar.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun isNetworkAvailable(): Boolean {
        val cm =
            requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
        return (capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))
    }

    companion object {
        fun newInstance() = LocationsFragment()
    }

}