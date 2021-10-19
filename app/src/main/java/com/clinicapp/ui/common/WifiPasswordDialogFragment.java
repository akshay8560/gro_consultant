package com.clinicapp.ui.common;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.clinicapp.databinding.FragmentWifiDialogPasswordBinding;
import com.clinicapp.models.WifiEntry;
import com.clinicapp.providers.AsyncResponse;
import com.clinicapp.utilities.BaseDialogFragment;
import com.clinicapp.utilities.Utils;

public class WifiPasswordDialogFragment extends BaseDialogFragment {
    private @NonNull FragmentWifiDialogPasswordBinding views;
    private WifiEntry wifiEntry;
    private WifiPasswordDialogViewModel viewModel;

    public static WifiPasswordDialogFragment getInstance(WifiEntry entry){
        final WifiPasswordDialogFragment fragment = new WifiPasswordDialogFragment();

        final Bundle bundle = new Bundle();
        bundle.putParcelable(DATA,entry);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wifiEntry = getArguments().getParcelable(DATA);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        views = FragmentWifiDialogPasswordBinding.inflate(inflater, container, false);
        positionDialogToTop();
        return views.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(WifiPasswordDialogViewModel.class);
        viewModel.init(wifiEntry);
        initViews();
    }

    private void initViews() {
        views.txtWifiName.setText("For "+ wifiEntry.getDetails().SSID);
        views.btJoin.setOnClickListener(this::handleJoin);
        views.btCancel.setOnClickListener(v->dismiss());
        viewModel.getWifiStateLiveData().observe(getViewLifecycleOwner(),this::handleConnectionResult);
    }

    private void handleConnectionResult(AsyncResponse<Boolean, Exception> response) {
        views.btJoin.setVisibility(response.isLoading() ? View.INVISIBLE:View.VISIBLE);
        views.progressLoader.setVisibility(response.isLoading() ? View.VISIBLE:View.GONE);
         if (response.isError()){
            Utils.notify(getContext(),response.message);
         } else if(response.isSuccess()){
             Toast.makeText(getContext(),"Connection successful.",Toast.LENGTH_SHORT).show();
             dismiss();
         }
    }

    private void handleJoin(View view) {
        viewModel.connect(views.inputPassword.getText().toString());
    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if(!viewModel.getWifiStateLiveData().getValue().isSuccess()){
            new WifiDialogFragment().show(getParentFragmentManager(),null);
        }
    }
}
