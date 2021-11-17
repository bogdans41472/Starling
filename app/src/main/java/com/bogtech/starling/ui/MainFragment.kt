package com.bogtech.starling.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bogtech.network.savings.model.TransferResponse
import com.bogtech.starling.R
import com.bogtech.starling.databinding.FragmentMainBinding

import com.bogtech.starling.ui.viewmodel.MainFragmentViewModel
import com.bogtech.starling.ui.viewmodel.MainViewModelFactory
import com.google.android.material.snackbar.Snackbar

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
    ): View {
        val binding: FragmentMainBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_main,
            container,
            false
        )
        val view = binding.root
        binding.lifecycleOwner = this
        setupObservers(binding, view)
        setupButtons(binding, view)
        return view
    }

    private fun setupButtons(binding: FragmentMainBinding, view: View) {
        binding.buttonTransfer.setOnClickListener {
            fragmentViewModel.transferRoundUpAmount()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupObservers(binding: FragmentMainBinding, view: View) {
        fragmentViewModel.totalRoundUpAmountLiveData.observe(requireActivity()) { amount ->
            binding.roundupValue.text = buildString {
                append(amount.currency)
                append(" ")
                append(amount.minorUnits)
            }
        }
        fragmentViewModel.transferState.observe(requireActivity()) { transferResponse ->
            showMessage(transferResponse, view)
        }
        fragmentViewModel.loadingProcess.observe(requireActivity()) { shouldShow ->
            loading(binding, shouldShow)
        }
        fragmentViewModel.errorLiveData.observe(requireActivity()) { exception ->
            binding.errorText.text = "${exception.errorEnum}: ${exception.developerMessage}"
        }
    }

    private fun showMessage(transferResponse: TransferResponse, view: View) {
        Snackbar.make(
            view,
            "${transferResponse.transferUid} success state: ${transferResponse.success}",
            Snackbar.LENGTH_LONG)
            .show()
    }

    private fun loading(binding: FragmentMainBinding, shouldShow: Boolean) {
        if (shouldShow) {
            binding.loadingSpinner.visibility = View.VISIBLE
        } else {
            binding.loadingSpinner.visibility = View.INVISIBLE
        }
    }

    override fun onStart() {
        super.onStart()
        fragmentViewModel.onStart(this)
    }

    override fun onStop() {
        super.onStop()
        fragmentViewModel.onStop(this)
    }

    private fun setupViewModel() {
        val factory = MainViewModelFactory()
        fragmentViewModel = ViewModelProvider(this, factory).get(MainFragmentViewModel::class.java)
    }

    companion object {
        const val FRAGMENT_TAG = "MAIN_FRAGMENT"
    }
}