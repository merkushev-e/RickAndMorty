package com.testtask.rickandmorty.presentation.character

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import com.testtask.rickandmorty.App
import com.testtask.rickandmorty.R
import com.testtask.rickandmorty.data.retrofit.model.Location
import com.testtask.rickandmorty.data.retrofit.model.Origin
import com.testtask.rickandmorty.databinding.FragmentCharacterDetailsBinding
import com.testtask.rickandmorty.databinding.FragmentCharactersBinding
import com.testtask.rickandmorty.domain.AppState
import com.testtask.rickandmorty.domain.model.CharactersData
import com.testtask.rickandmorty.presentation.character.adapter.CharactersAdapter
import com.testtask.rickandmorty.presentation.character.viewModel.CharacterDetailsViewModel
import com.testtask.rickandmorty.presentation.character.viewModel.CharactersViewModel
import kotlinx.android.synthetic.main.characters_item_rv.*
import kotlinx.android.synthetic.main.fragment_character_details.view.*

class CharacterDetailsFragment : Fragment() {

    private lateinit var viewModel: CharacterDetailsViewModel

    private var _binding: FragmentCharacterDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var character: CharactersData
    private val adapter: CharactersAdapter by lazy {
        CharactersAdapter()
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
        if (arguments != null){
            character = arguments?.getParcelable(CHARACTER_EXTRA)!!
            showInfo(character)
        }



        getData()

    }

    private fun showInfo(character: CharactersData) {
        with(binding){
            characterGender.text = character.gender
            characterName.text = character.name
            characterSpecies.text = character.species
            characterImage.load(character.image){
                error(R.drawable.ic_load_error_vector)
                placeholder(R.drawable.ic_no_photo_vector)
            }
            characterOrigin.text = character.origin.name
            characterStatus.text =character.status
        }
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.SuccessDetails -> {
//                showViewSuccess(appState)
            }
            is AppState.Error -> {

//                showErrorScreen(appState.error.message)
            }
            else -> {}
        }

    }


    private fun getData() {
//        viewModel.liveData.observe(viewLifecycleOwner) { appState ->
//            renderData(appState)
//        }
    }




    companion object {
        const val CHARACTER_EXTRA = "Character"
        fun newInstance(bundle: Bundle) = CharacterDetailsFragment().apply {
            arguments = bundle
        }
    }

}