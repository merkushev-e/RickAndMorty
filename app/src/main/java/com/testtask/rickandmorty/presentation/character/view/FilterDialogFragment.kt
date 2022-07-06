package com.testtask.rickandmorty.presentation.character.view

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.testtask.rickandmorty.App
import com.testtask.rickandmorty.databinding.FilterDialogFragmentBinding
import com.testtask.rickandmorty.presentation.character.view.CharactersFragment.Companion.INITIAL_VALUE
import com.testtask.rickandmorty.presentation.character.viewModel.CharactersViewModel
import com.testtask.rickandmorty.presentation.character.viewModel.states.GenderState
import com.testtask.rickandmorty.presentation.character.viewModel.states.StatusState


class FilterDialogFragment() : BottomSheetDialogFragment() {

    private var _binding: FilterDialogFragmentBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: CharactersViewModel

    private var status: StatusState = StatusState.NONE
    private var genderState: GenderState = GenderState.NONE

    private var onSearchClickListener: OnSearchClickListener? = null
    internal fun setOnSearchClickListener(listener: OnSearchClickListener) {
        onSearchClickListener = listener
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FilterDialogFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initButtons()
        initRadioButtons()
    }

    private fun initRadioButtons() {

        binding.characterName.setText("")


        binding.rgStatus.setOnCheckedChangeListener { radioGroup, i ->
            status = when (i) {
                binding.rbAlive.id -> {
                    StatusState.ALIVE
                }
                binding.rbDead.id -> {
                    StatusState.DEAD
                }
                binding.rbUnknown.id -> {
                    StatusState.UNKNOWN
                }
                else -> {
                    StatusState.NONE
                }
            }
        }

        binding.rgGender.setOnCheckedChangeListener { radioGroup, i ->
            genderState = when (i) {
                binding.rbFemale.id -> GenderState.FEMALE
                binding.rbMale.id -> GenderState.MALE
                binding.rbUnknownGender.id -> GenderState.UNKNOWN
                binding.rbGenderless.id -> GenderState.GENDERLESS
                else -> GenderState.NONE
            }
        }

    }

    private fun initButtons() {
        binding.buttonClear.setOnClickListener {
            binding.rgGender.clearCheck()
            binding.rgStatus.clearCheck()
            binding.characterName.setText(INITIAL_VALUE)
        }

        binding.buttonApply.setOnClickListener {
            onSearchClickListener?.onClick(
                status,
                genderState,
                binding.characterName.text.toString()
            )
            dismiss()
        }
    }



    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (activity is DialogInterface.OnDismissListener) {
            (activity as DialogInterface.OnDismissListener).onDismiss(dialog)
        }
    }

        fun interface OnSearchClickListener {
        fun onClick(statusState: StatusState, genderState: GenderState, textQuery: String)
    }



}