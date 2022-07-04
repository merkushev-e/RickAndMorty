package com.testtask.rickandmorty.presentation.episodes.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.testtask.rickandmorty.App
import com.testtask.rickandmorty.R
import com.testtask.rickandmorty.databinding.FragmentCharacterDetailsBinding
import com.testtask.rickandmorty.databinding.FragmentEpisodeDetailBinding
import com.testtask.rickandmorty.domain.AppState
import com.testtask.rickandmorty.domain.model.CharactersData
import com.testtask.rickandmorty.domain.model.EpisodeData
import com.testtask.rickandmorty.presentation.MainActivity.Companion.SUB_DETAILS_FRAGMENTS
import com.testtask.rickandmorty.presentation.character.adapter.CharactersDetailsAdapter
import com.testtask.rickandmorty.presentation.character.view.CharacterDetailsFragment
import com.testtask.rickandmorty.presentation.character.viewModel.CharacterDetailsViewModel
import com.testtask.rickandmorty.presentation.episodes.adapter.EpisodeDetailsAdapter
import com.testtask.rickandmorty.presentation.episodes.viewmodel.EpisodeDetailViewModel
import kotlinx.android.synthetic.main.fragment_episode_detail.*

class EpisodeDetailFragment : Fragment() {

    private lateinit var viewModel: EpisodeDetailViewModel

    private var _binding: FragmentEpisodeDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var episode: EpisodeData
    private val adapter: EpisodeDetailsAdapter by lazy {
        EpisodeDetailsAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelFactory = App.instance.component.getViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory)[EpisodeDetailViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEpisodeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        episode = arguments?.getParcelable(EPISODE_EXTRA) ?: EpisodeData()
        showInfo(episode)

        binding.swipeRefreshLayout.setOnRefreshListener {
            getData()
            stopRefreshAnimationIfNeeded()
        }

        initAdapter()
        getData()
        initTryAgainButton()
    }

    private fun showInfo(episodeData: EpisodeData) {

        with(binding) {
            episodeDate.text = episodeData.air_date
            episodeName.text = episodeData.name
            episode.text = episodeData.episode
        }

    }


    private fun getData() {
        viewModel.getCharacterList(episode)
        viewModel.liveData.observe(viewLifecycleOwner) { appState ->
            renderData(appState)
        }
    }

    private fun initAdapter() {
        with(binding) {
            recyclerViewCharacters.adapter = adapter
            recyclerViewCharacters.layoutManager = GridLayoutManager(requireContext(), 2)
        }
        adapter.listener = EpisodeDetailsAdapter.OnListItemClickListener { data ->
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
                ).addToBackStack("")
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
        const val EPISODE_EXTRA = "Episode"
        fun newInstance(bundle: Bundle) = EpisodeDetailFragment().apply {
            arguments = bundle
        }
    }
}