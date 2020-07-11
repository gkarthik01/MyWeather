package com.karthik.myweather.ui.view;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.karthik.myweather.Permission;
import com.karthik.myweather.R;
import com.karthik.myweather.databinding.MainFragmentBinding;
import com.karthik.myweather.ui.viewModel.MainViewModel;
import com.karthik.myweather.utils.LocationLiveData;

import javax.inject.Inject;

public class MainFragment extends BaseFragment {

    private static final int MY_PERMISSIONS_REQUEST_CODE = 99;
    private MainViewModel mViewModel;
    private MainFragmentBinding binding;
    private LocationManager locationManager;

    @Inject
    LocationLiveData locationLiveData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false);
        binding.setViewModel(mViewModel);
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);
        mViewModel.init();
        registerForLocationPermission();
        registerForGetLocation();
        registerNavEvent();
        registerValidation();
        registerForLoadingIndicator(mViewModel);
        registerForErrorDialog(mViewModel);
    }

    private void registerLocationUpdates() {
        locationLiveData.observe(this, location -> {
            mViewModel.onLocation(location);
            locationLiveData.removeObservers(this);
        });
    }

    private void registerValidation() {
        mViewModel.isValid.observe(this, isValid -> binding.btnsubmit.setEnabled(isValid));

    }

    private void registerNavEvent() {
        mViewModel.navEvent.observe(this, e -> {
            switch (e) {
                case LocationToCity:
                    navController.navigate(R.id.action_mainFragment_to_selectLocationFragment);
                    break;
            }
        });
    }

    private void registerForGetLocation() {
        mViewModel.locationRequest.observe(this, e -> {
            registerLocationUpdates();
        });
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mViewModel.onPermission(Permission.GRANTED);
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            mViewModel.onPermission(Permission.DENIED);
            //todo show alert dialog
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void registerForLocationPermission() {
        mViewModel.permissionRequest.observe(this, e -> {
            requestLocationPermission();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mViewModel.onPermission(Permission.GRANTED);
                } else {
                    mViewModel.onPermission(Permission.DENIED);
                    //todo show alert dialog
                }
                return;
        }
    }


}