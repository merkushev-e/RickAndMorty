package com.testtask.rickandmorty.presentation.character

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.testtask.rickandmorty.App
import com.testtask.rickandmorty.R
import com.testtask.rickandmorty.data.retrofit.model.Location
import com.testtask.rickandmorty.data.retrofit.model.Origin
import com.testtask.rickandmorty.databinding.FragmentCharacterDetailsBinding
import com.testtask.rickandmorty.databinding.FragmentCharactersBinding
import com.testtask.rickandmorty.domain.AppState
import com.testtask.rickandmorty.domain.model.CharactersData
import com.testtask.rickandmorty.domain.model.EpisodeData
import com.testtask.rickandmorty.presentation.character.adapter.CharactersAdapter
import com.testtask.rickandmorty.presentation.character.adapter.CharactersDetailsAdapter
import com.testtask.rickandmorty.presentation.character.viewModel.CharacterDetailsViewModel
import com.testtask.rickandmorty.presentation.character.viewModel.CharactersViewModel
import kotlinx.android.synthetic.main.characters_item_rv.*
import kotlinx.android.synthetic.main.fragment_character_details.view.*

class CharacterDetailsFragment : Fragment() {

    private lateinit var viewModel: CharacterDetailsViewModel

    private var _binding: FragmentCharacterDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var character: CharactersData
    private val adapter: CharactersDetailsAdapter by lazy {
        CharactersDetailsAdapter()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelFactory = App.instance.component.getViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory)[CharacterDetailsViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCharacterDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            character = arguments?.getParcelable(CHARACTER_EXTRA) ?: CharactersData(location = CharactersData.Location(), origin = CharactersData.Origin())
            showInfo(character)

        initAdapter()
        getData()

    }

    private fun initAdapter() {
        with(binding){
            recyclerViewEpisode.adapter = adapter
            recyclerViewEpisode.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
        }
    }

    private fun showInfo(character: CharactersData) {
        with(binding){
            characterGender.text = character.gender
            characterName.text = character.name
            characterSpecies.text = character.species
            characterImage.load(character.image){
                error(R.drawable.ic_load_error_vector)
                placeholder(R.drawable.ic_no_photo_vector)
                transformations(CircleCropTransformation())
                build()
            }
            characterOrigin.text = character.origin.name
            characterStatus.text = character.status
            characterLocation.text = character.location.name
            characterLocation.setOnClickListener {
//                requireActivity().supportFragmentManager.beginTransaction()
//                    .replace(R.id.container,)
            }
        }
    }



    private fun getData() {
        viewModel.getEpisodesList(character)
        viewModel.liveData.observe(viewLifecycleOwner) { appState ->
            renderData(appState)
        }
    }
    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.SuccessDetails -> {
                adapter.setData(appState.data)
                showViewSuccess()
            }
            is AppState.Loading ->{
                showViewLoading()
            }
            is AppState.Error -> {
                showViewError()
            }
            else -> {}
        }

    }


    private fun showViewSuccess(){
        binding.recyclerViewEpisode.visibility = View.VISIBLE
        binding.loadStateView.progressBar.visibility = View.GONE
        binding.loadStateView.messageTextView.visibility = View.GONE
        binding.loadStateView.tryAgainButton.visibility = View.GONE
    }

    private fun showViewLoading() {
        binding.recyclerViewEpisode.visibility = View.GONE
        binding.loadStateView.progressBar.visibility = View.VISIBLE
        binding.loadStateView.messageTextView.visibility = View.GONE
        binding.loadStateView.tryAgainButton.visibility = View.GONE
    }


    private fun showViewError() {
        binding.recyclerViewEpisode.visibility = View.GONE
        binding.loadStateView.progressBar.visibility = View.GONE
        binding.loadStateView.messageTextView.visibility = View.VISIBLE
        binding.loadStateView.tryAgainButton.visibility = View.VISIBLE
    }




    companion object {
        const val CHARACTER_EXTRA = "Character"
        fun newInstance(bundle: Bundle) = CharacterDetailsFragment().apply {
            arguments = bundle
        }
    }

}