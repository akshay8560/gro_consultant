package com.clinicapp.ui.common;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.clinicapp.adapters.WifiAdapter;
import com.clinicapp.databinding.FragmentWifiDialogBinding;
import com.clinicapp.models.WifiEntry;
import com.clinicapp.providers.WifiProvider;
import com.clinicapp.ui.home.viewmodels.MainViewModel;
import com.clinicapp.utilities.BaseDialogFragment;
import com.clinicapp.utilities.Utils;
import com.emreeran.permissionlivedata.PermissionLiveData;
import com.emreeran.permissionlivedata.Status;

import java.util.List;

public class WifiDialogFragment extends BaseDialogFragment {
    private MainViewModel viewModel;
    private FragmentWifiDialogBinding views;
    private WifiAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        views = FragmentWifiDialogBinding.inflate(inflater, container, false);
        getDialog().getWindow().setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL);
        positionDialogToTop();
        return views.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        initViews();
    }


    private void initViews() {
        adapter = new WifiAdapter(this::handleWifiSelected);
        views.rvWifi.setLayoutManager(new LinearLayoutManager(getContext()));
        views.rvWifi.setAdapter(adapter);

        requestPermission();
    }

    private void handleWifiSelected(WifiEntry wifiEntry) {
        WifiPasswordDialogFragment.getInstance(wifiEntry)
                .show(getParentFragmentManager(),null);
        dismiss();
    }

    private void requestPermission() {
        PermissionLiveData.create(this,Manifest.permission.ACCESS_FINE_LOCATION)
            .observe(getViewLifecycleOwner(), result->{
                if(result.getStatus() == Status.RECEIVED){
                    startScan();
                } else if(result.getStatus() != Status.PENDING){
                    Utils.notify(getContext(),"Permission denied. Please grant fine location permission.");
                    dismiss();;
                }
        });
    }

    private void startScan(){
        WifiProvider.getInstance(getActivity().getApplication())
                .startScan()
                .observe(getViewLifecycleOwner(),this::handleWifiScanResults);
    }

    private void handleWifiScanResults(List<WifiEntry> wifiEntries) {
        views.groupLoader.setVisibility(wifiEntries.size() == 0 ? View.VISIBLE:View.GONE);
        adapter.setData(wifiEntries);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        WifiProvider.getInstance(getActivity().getApplication())
                .stopScan();
    }
}
