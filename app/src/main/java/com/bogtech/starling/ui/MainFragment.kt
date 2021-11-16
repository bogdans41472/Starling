package com.bogtech.starling.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bogtech.starling.R
import com.bogtech.starling.databinding.FragmentMainBinding

import com.bogtech.starling.ui.viewmodel.MainFragmentViewModel
import com.bogtech.starling.ui.viewmodel.MainViewModelFactory

class MainFragment : Fragment() {

    private lateinit var fragmentViewModel: MainFragmentViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setupViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentMainBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_main,
            container,
            false
        )
        val view = binding.root
        binding.lifecycleOwner = this
        setupObservers(binding, view)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setupObservers(binding: FragmentMainBinding, view: View) {
        fragmentViewModel.totalRoundUpAmountLiveData.observe(requireActivity()) {
            binding.roundupValue.text = buildString {
                append(it.currency)
                append(" ")
                append(it.minorUnits.toInt())
            }
        }
        fragmentViewModel.errorLiveData.observe(requireActivity()) {
            binding.errorText.text = it.message
        }
    }

    private fun setupViewModel() {
        val factory = MainViewModelFactory()
        fragmentViewModel = ViewModelProvider(this, factory).get(MainFragmentViewModel::class.java)
    }

    companion object {
        const val FRAGMENT_TAG = "MAIN_FRAGMENT"
    }
}