package com.clinicapp.ui.home;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.clinicapp.R;
import com.clinicapp.databinding.FragmentSearchPatientsBinding;
import com.clinicapp.models.Patient;
import com.clinicapp.models.SearchPatientsResponse;
import com.clinicapp.ui.home.viewmodels.MainViewModel;
import com.clinicapp.ui.home.viewmodels.SearchPatientViewModel;
import com.clinicapp.utilities.BaseFragment;
import com.clinicapp.utilities.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SearchPatientsFragment extends BaseFragment {
    private SearchPatientViewModel viewModel;
    private MainViewModel mainViewModel;
    private FragmentSearchPatientsBinding views;


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        views = FragmentSearchPatientsBinding.inflate(getLayoutInflater(),container,false);
        views.form.txtTitle.setText("Search Patient");
        return views.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        viewModel = new ViewModelProvider(this).get(SearchPatientViewModel.class);
        init();
    }

    private void init(){
        views.findPatient.setOnClickListener(this::handleSearch);

        viewModel.getApiLiveData()
            .observe(getViewLifecycleOwner(), result -> {
                swapButtonAndLoader(result.isLoading());
                setViewState(!result.isLoading(), views.form.firstname, views.form.lastname,
                       views.form.phone);

                if(result.isError()) {
                    Utils.notify(getActivity(), result.message);
                } else if(result.isSuccess() && result.isFresh()){
                    //Use pop as we want to act on the result only when it's fresh.
                    final SearchPatientsResponse searchResult = result.pop();
                    handleSearchResults(searchResult.getPatients());
                }
            });
    }

    private void handleSearchResults(List<Patient> patients) {
        if(patients.size() > 0){
            mainViewModel.setSearchResult(patients);
            Navigation.findNavController(views.getRoot())
                    .navigate(R.id.action_searchPatientFragment_to_resultScreen);
        } else {
            Utils.notify(getActivity(),"No patients found.");
        }
    }


    private void handleSearch(View view) {
        String firstName = views.form.firstname.getText().toString();
        String lastName = views.form.lastname.getText().toString();
        String phone = views.form.phone.getText().toString();

//        viewModel.searchPatient(firstName,lastName, phone);
        List<Patient> patients=new ArrayList<>();
        patients.add(new Patient(1,"Test","LastTest","ab","20"
                ,"01-01-2000","Male","123456",29  ));

        handleSearchResults(patients);

    }

    private void swapButtonAndLoader(boolean isLoading){
        views.progressBar.setVisibility(isLoading ? View.VISIBLE:View.INVISIBLE);
        views.findPatient.setVisibility(isLoading ? View.INVISIBLE:View.VISIBLE);
    }
}