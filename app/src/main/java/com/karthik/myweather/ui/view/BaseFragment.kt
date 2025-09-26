package com.karthik.myweather.ui.view

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.karthik.myweather.R
import com.karthik.myweather.ui.viewModel.BaseViewModel

abstract class BaseFragment : Fragment() {

    protected lateinit var navController: NavController

    protected fun registerForErrorDialog(viewModel: BaseViewModel) {
        viewModel.error.observe(this, Observer { showErrorDialog() })
    }

    protected fun registerForLoadingIndicator(viewModel: BaseViewModel) {
        viewModel.isLoading.observe(this, Observer { isLoading: Boolean ->
            if (isLoading) {
                showProgressDialog()
            } else {
                dismissProgressDialog()
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navController = Navigation.findNavController(requireActivity(), R.id.navHostFragment)
    }

    private fun showProgressDialog() {
        val prev = requireActivity().supportFragmentManager
                .findFragmentByTag(Progress_Dialog_Tag)
        if (prev == null) {
            ProgressDialogFragment().show(
                    requireActivity().supportFragmentManager, Progress_Dialog_Tag)
        }
    }

    private fun dismissProgressDialog() {
        val prev = requireActivity().supportFragmentManager
                .findFragmentByTag(Progress_Dialog_Tag)
        val df = prev as DialogFragment
        df.dismiss()
    }

    private fun showErrorDialog() {
        AlertDialog.Builder(requireActivity())
                .setTitle("Error")
                .setMessage("An Unknown error occured")
                .setCancelable(true)
                .setNeutralButton("OK") { dialogInterface, _ -> dialogInterface.dismiss() }
                .show()
    }

    protected fun setTitle(title: String?) {
        (activity as AppCompatActivity?)!!.supportActionBar?.title = title
    }

    companion object {
        private const val Progress_Dialog_Tag = "Progress_Dialog_Tag"
    }
}