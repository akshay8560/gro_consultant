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
import com.clinicapp.databinding.FragmentNewPatientBinding;
import com.clinicapp.models.AddPatientResponse;
import com.clinicapp.ui.home.viewmodels.MainViewModel;
import com.clinicapp.ui.home.viewmodels.NewPatientViewModel;
import com.clinicapp.utilities.BaseFragment;
import com.clinicapp.utilities.PostTextWatcher;
import com.clinicapp.utilities.Utils;

import java.util.Calendar;

import kotlin.Pair;

public class NewPatientFragment extends BaseFragment {
    private FragmentNewPatientBinding views;
    private NewPatientViewModel viewModel;
    private MainViewModel mainViewModel;
    private static final String TAG = "NewPatientFragment";




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        views = FragmentNewPatientBinding.inflate(getLayoutInflater(),container,false);
        views.form.txtTitle.setText(R.string.create_new_patient);
        return views.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(NewPatientViewModel.class);
        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        init();
    }


    private void init() {
        new PostTextWatcher(views.form.firstname, this::handleFirstNameChange);
        new PostTextWatcher(views.form.lastname, this::handleLastNameChange);
        new PostTextWatcher(views.form.phone, this::handlePhoneChange);

        views.form.dob.setOnClickListener(this::showDatePicker);

        views.addPatient.setOnClickListener(this::addPatient);

        viewModel.getApiLiveData()
                .observe(getViewLifecycleOwner(), result -> {
                    swapButtonAndLoader(result.isLoading());
                    setViewState(!result.isLoading(), views.form.firstname, views.form.lastname,
                            views.form.gender, views.form.dob, views.form.phone);
                    if(result.isError()) {
                        handleApiError(result.value, result.message);
                    } else if( result.isSuccess() && result.value.getStatus()) {

                        handleSuccessApiResponse();

                    }
                });
    }

    private void handleSuccessApiResponse(){
        mainViewModel.onPatientAdded(viewModel.getPatient());
        Navigation.findNavController(views.getRoot())
                .navigate(R.id.action_newPatientFragment_to_selectShootPosition);
    }


    private void handleApiError(AddPatientResponse response, String message) {
        if(!response.showFieldError()){
            Utils.notify(getActivity(), message);
        } else {
            showError(views.form.firstnameErr, response.getFirstNameError());
            showError(views.form.lastnameErr, response.getLastNameError());
            showError(views.form.phoneErr, response.getPhoneError());
        }
    }


    private void swapButtonAndLoader(boolean isLoading){
        views.progressBar.setVisibility(isLoading ? View.VISIBLE:View.INVISIBLE);
        views.addPatient.setVisibility(isLoading ? View.INVISIBLE:View.VISIBLE);
    }


    private boolean handleFirstNameChange(String value){
        final Pair<Boolean,String> valResult = viewModel.validateFirstName(value);
        return handleValidation(views.form.firstnameErr, valResult);
    }

    private boolean handleLastNameChange(String s) {
        final Pair<Boolean,String> valResult = viewModel.validateLastName(s);
        return handleValidation(views.form.lastnameErr, valResult);
    }

    private boolean handlePhoneChange(String s) {
        final Pair<Boolean,String> valResult = viewModel.validatePhone(s);
        return handleValidation(views.form.phoneErr, valResult);
    }


    private void showDatePicker(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DATE);
        new DatePickerDialog(getActivity(), (datePicker, year1, month1, day) -> {
            month1++;
            String monthOfYear = month1 < 10 ? "0" + month1 : month1 + "";
            views.form.dob.setText(monthOfYear + "/" + day + "/" + year1);
        }, year, month, date).show();
    }

    private void addPatient(View view) {

        final String firstName = views.form.firstname.getText().toString();
        final String lastName = views.form.lastname.getText().toString();
        final String dateOfBirth = views.form.dob.getText().toString();
        final String phone = views.form.phone.getText().toString();

        final boolean isValidFirstName = handleFirstNameChange(firstName);
        final boolean isValidLastName = handleLastNameChange(lastName);
        final boolean isValidPhone = handlePhoneChange(phone);
        if(isValidFirstName && isValidLastName && isValidPhone)
        handleSuccessApiResponse();
//            viewModel.addPatient(firstName,lastName, dateOfBirth,"gender", phone);
    }


}