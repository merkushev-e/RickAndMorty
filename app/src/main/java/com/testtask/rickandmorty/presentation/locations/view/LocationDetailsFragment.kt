package com.testtask.rickandmorty.presentation.locations.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.testtask.rickandmorty.App
import com.testtask.rickandmorty.R
import com.testtask.rickandmorty.databinding.FragmentLocationsDetailsBinding
import com.testtask.rickandmorty.domain.AppState
import com.testtask.rickandmorty.domain.model.LocationData
import com.testtask.rickandmorty.presentation.MainActivity.Companion.SUB_DETAILS_FRAGMENTS
import com.testtask.rickandmorty.presentation.character.view.CharacterDetailsFragment
import com.testtask.rickandmorty.presentation.episodes.viewmodel.EpisodeDetailViewModel
import com.testtask.rickandmorty.presentation.locations.adapter.LocationDetailsAdapter
import com.testtask.rickandmorty.presentation.locations.viewmodels.LocationsDetailViewModel
import kotlinx.android.synthetic.main.location_item_rv.*


class LocationDetailsFragment : Fragment() {

    private lateinit var viewModel: LocationsDetailViewModel

    private var _binding: FragmentLocationsDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var location: LocationData
    private val adapter: LocationDetailsAdapter by lazy {
        LocationDetailsAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelFactory = App.instance.component.getViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory)[LocationsDetailViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLocationsDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        location = arguments?.getParcelable(Location_EXTRA) ?: LocationData()
        showInfo(location)

        binding.swipeRefreshLayout.setOnRefreshListener {
            getData()
            stopRefreshAnimationIfNeeded()
        }

        initAdapter()
        getData()
        initTryAgainButton()
    }


    private fun showInfo(location: LocationData) {

        with(binding) {
            locationName.text  = location.name
            locationDimension.text = location.dimension
            locationType.text = location.type
        }

    }


    private fun getData() {
        viewModel.getCharactersList(location)
        viewModel.liveData.observe(viewLifecycleOwner) { appState ->
            renderData(appState)
        }
    }

    private fun initAdapter() {
        with(binding) {
            recyclerViewCharacters.adapter = adapter
            recyclerViewCharacters.layoutManager = GridLayoutManager(requireContext(), 2)
        }
        adapter.listener = LocationDetailsAdapter.OnListItemClickListener { data ->
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.container,
                    CharacterDetailsFragment.newInstance(Bundle().apply {
                        putParcelable(
                            CharacterDetailsFragment.CHARACTER_EXTRA,
                            data
                        )
                    }),
                    SUB_DETAILS_FRAGMENTS
                )
                .addToBackStack("")
                .commit()
        }
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.SuccessDetailsCharacter -> {
                stopRefreshAnimationIfNeeded()
                adapter.setData(appState.data)
                showViewSuccess()
            }
            is AppState.Loading -> {
                stopRefreshAnimationIfNeeded()
                showViewLoading()
            }
            is AppState.Error -> {
                stopRefreshAnimationIfNeeded()
                appState.error.message?.let { showViewError(it) }
            }
            else -> {}
        }

    }


    private fun showViewSuccess() {
        binding.recyclerViewCharacters.visibility = View.VISIBLE
        binding.loadStateView.progressBar.visibility = View.GONE
        binding.loadStateView.messageTextView.visibility = View.GONE
        binding.loadStateView.tryAgainButton.visibility = View.GONE
    }

    private fun showViewLoading() {
        binding.recyclerViewCharacters.visibility = View.GONE
        binding.loadStateView.progressBar.visibility = View.VISIBLE
        binding.loadStateView.messageTextView.visibility = View.GONE
        binding.loadStateView.tryAgainButton.visibility = View.GONE
    }


    private fun showViewError(error: String) {
        binding.recyclerViewCharacters.visibility = View.GONE
        binding.loadStateView.progressBar.visibility = View.GONE
        binding.loadStateView.messageTextView.visibility = View.VISIBLE
        binding.loadStateView.tryAgainButton.visibility = View.VISIBLE
        binding.loadStateView.messageTextView.text = error

    }


    private fun initTryAgainButton() {
        binding.loadStateView.tryAgainButton.setOnClickListener {
            stopRefreshAnimationIfNeeded()
            getData()
        }
    }

    private fun stopRefreshAnimationIfNeeded() {
        if (binding.swipeRefreshLayout.isRefreshing) {
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }




    companion object {

        const val Location_EXTRA = "Locations"

        fun newInstance(bundle: Bundle) = LocationDetailsFragment().apply {
            arguments = bundle
        }
    }
}