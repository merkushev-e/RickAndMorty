package com.testtask.rickandmorty.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.testtask.rickandmorty.App
import com.testtask.rickandmorty.R
import com.testtask.rickandmorty.databinding.ActivityMainBinding
import com.testtask.rickandmorty.presentation.character.view.CharactersFragment
import com.testtask.rickandmorty.presentation.episodes.view.EpisodesFragment
import com.testtask.rickandmorty.presentation.locations.LocationsFragment

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        App.instance.component.inject(this)



        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, CharactersFragment.newInstance(),CHARACTERS_FRAGMENTS)
                .commit()
        }
        initBottomNavigationView()
    }

    private fun initBottomNavigationView() {
        binding.bottomNavigationView.selectedItemId = R.id.characters
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.characters -> {
                    for (i in 0 until supportFragmentManager.backStackEntryCount) {
                        supportFragmentManager.popBackStack()
                    }
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, CharactersFragment.newInstance(), CHARACTERS_FRAGMENTS)
                        .commit()
                    supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    true
                }
                R.id.episodes  ->{
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, EpisodesFragment.newInstance())
                        .addToBackStack(null)
                        .commit()
                    true
                }

                R.id.locations->{
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, LocationsFragment.newInstance())
                        .addToBackStack(null)
                        .commit()
                    true
                }
                else -> false
            }
        }
    }

    override fun onBackPressed() {

        val currentFragment = supportFragmentManager.findFragmentByTag(CHARACTERS_FRAGMENTS)

        if (currentFragment != null && currentFragment.isVisible) {
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            super.onBackPressed()
        }else{
            binding.bottomNavigationView.selectedItemId = R.id.characters
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    companion object{
        private const val CHARACTERS_FRAGMENTS = "Characters"
    }
}