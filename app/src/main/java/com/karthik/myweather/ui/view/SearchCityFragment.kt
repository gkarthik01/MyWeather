package com.karthik.myweather.ui.view

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.karthik.myweather.Event
import com.karthik.myweather.Permission
import com.karthik.myweather.R
import com.karthik.myweather.databinding.SearchCityFragmentBinding
import com.karthik.myweather.navigation.NavEvent
import com.karthik.myweather.ui.viewModel.SearchCityViewModel
import com.karthik.myweather.utils.LocationLiveData
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchCityFragment : BaseFragment() {
    private val mViewModel: SearchCityViewModel by viewModels()
    private lateinit var binding: SearchCityFragmentBinding

    @Inject
    lateinit var locationLiveData: LocationLiveData
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.search_city_fragment, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = mViewModel
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel.init()
        registerForLocationPermission()
        registerForGetLocation()
        registerNavEvent()
        registerForLoadingIndicator(mViewModel)
        registerForErrorDialog(mViewModel)
    }

    private fun registerLocationUpdates() {
        locationLiveData.observe(this, Observer<Location> { location: Location ->
            mViewModel.onLocation(location)
            locationLiveData.removeObservers(this)
        })
    }

    private fun registerNavEvent() {
        mViewModel.navEvent.observe(this, Observer { e: NavEvent? ->
            when (e) {
                NavEvent.LocationToCity -> navController.navigate(R.id.action_mainFragment_to_selectLocationFragment)
            }
        })
    }

    private fun registerForGetLocation() {
        mViewModel.locationRequest.observe(this, Observer { e: Event? -> registerLocationUpdates() })
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mViewModel.onPermission(Permission.GRANTED)
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            mViewModel.onPermission(Permission.DENIED)
            //todo show alert dialog
        } else {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_CODE)
        }
    }

    private fun registerForLocationPermission() {
        mViewModel.permissionRequest.observe(this, Observer { e: Event -> requestLocationPermission() })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mViewModel.onPermission(Permission.GRANTED)
                } else {
                    mViewModel.onPermission(Permission.DENIED)
                    //todo show alert dialog
                }
                return
            }
        }
    }

    companion object {
        private const val MY_PERMISSIONS_REQUEST_CODE = 99
    }
}