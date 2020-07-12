package com.karthik.myweather.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.karthik.myweather.R;
import com.karthik.myweather.di.ViewModelFactory;
import com.karthik.myweather.ui.viewModel.BaseViewModel;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public abstract class BaseFragment extends DaggerFragment {

    private static final String Progress_Dialog_Tag = "Progress_Dialog_Tag";

    @Inject
    protected ViewModelFactory viewModelFactory;

    protected NavController navController;
    private AlertDialog errorDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected void registerForErrorDialog(BaseViewModel viewModel) {
        viewModel.error.observe(this, error -> {
            showErrorDialog();
        });
    }

    protected void registerForLoadingIndicator(BaseViewModel viewModel) {
        viewModel.isLoading.observe(this, isLoading ->{
            if(isLoading){
                showProgressDialog();
            }else {
                dismissProgressDialog();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        navController = Navigation.findNavController(getActivity(), R.id.fragment_navHost);
    }

    protected void showProgressDialog(){
        Fragment prev = getActivity().getSupportFragmentManager()
                .findFragmentByTag(Progress_Dialog_Tag);
        if(prev==null){
            new ProgressDialogFragment().show(
                    getActivity().getSupportFragmentManager(), Progress_Dialog_Tag);
        }
    }

    protected void dismissProgressDialog(){
        Fragment prev = getActivity().getSupportFragmentManager()
                .findFragmentByTag(Progress_Dialog_Tag);
        if (prev != null) {
            DialogFragment df = (DialogFragment) prev;
            df.dismiss();
        }
    }

    protected void showErrorDialog(){
        errorDialog = new AlertDialog.Builder(getActivity())
                .setTitle("Error")
                .setMessage("An Unknown error occured")
                .setCancelable(true)
                .setNeutralButton("OK", (dialogInterface, i) -> {
                    errorDialog.dismiss();
                })
                .show();
    }

    protected void setTitle(String title) {
        ((AppCompatActivity) getActivity()).getSupportActionBar()
                .setTitle(title);
    }

}
