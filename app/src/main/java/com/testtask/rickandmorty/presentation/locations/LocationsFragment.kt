package com.testtask.rickandmorty.presentation.locations

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.testtask.rickandmorty.R

class LocationsFragment : Fragment() {

    companion object {
        fun newInstance() = LocationsFragment()
    }

    private lateinit var viewModel: LocationsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_locations, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LocationsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}