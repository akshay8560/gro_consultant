package com.clinicapp.ui.home.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.clinicapp.R;
import com.clinicapp.models.AddPatientRequest;
import com.clinicapp.models.AddPatientResponse;
import com.clinicapp.models.Patient;
import com.clinicapp.providers.AsyncResponse;
import com.clinicapp.utilities.BaseViewModel;

import java.util.HashMap;

import kotlin.Pair;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewPatientViewModel extends BaseViewModel {
    private MutableLiveData<AsyncResponse<AddPatientResponse,Exception>> apiLiveData;
    private Patient patient;
    public NewPatientViewModel(@NonNull Application application) {
        super(application);
    }

    public HashMap<Integer, String> validate(String firstName, String lastName, String dob, String gender, String phone){
        final HashMap<Integer, String> result = new HashMap<>();
        result.put(R.id.firstname, validateFirstName(firstName).getSecond());
        result.put(R.id.lastname, validateLastName(lastName).getSecond());
        //result.put(R.id.dob, validateDoB(dob).getSecond());
        //result.put(R.id.gender, validateGender(gender).getSecond());
        result.put(R.id.phone, validatePhone(phone).getSecond());
        return result;
    }

    public Pair<Boolean, String> validateFirstName(String value){
        return AddPatientRequest.validateFirstName(value);
    }

    public Pair<Boolean, String> validateLastName(String value){
        return AddPatientRequest.validateLastName(value);
    }

    public Pair<Boolean, String> validateDoB(String value){
        return AddPatientRequest.validateDateOfBirth(value);
    }

    public Pair<Boolean, String> validateGender(String value){
        return AddPatientRequest.validateGender(value);
    }

    public Pair<Boolean, String> validatePhone(String value){
        return AddPatientRequest.validatePhone(value);
    }



    public void addPatient(String firstName, String lastName, String dob, String gender, String phone){
        apiLiveData.postValue(AsyncResponse.loading());
        final AddPatientRequest request = new AddPatientRequest(firstName, lastName, dob, gender, phone);
        repository.api.addPatient(request)
                .enqueue(new Callback<AddPatientResponse>() {

                    @Override
                    public void onResponse(Call<AddPatientResponse> call, Response<AddPatientResponse> response) {
                        if(response.isSuccessful()){
                            final AddPatientResponse apiResponse = response.body();
                            if(apiResponse.getStatus()){
                                patient = request.toPatient(apiResponse.getPatientID());
                                apiLiveData.setValue(AsyncResponse.success(apiResponse));
                            } else {
                                apiLiveData.setValue(AsyncResponse.errorWithObj(apiResponse,"Something went wrong"));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AddPatientResponse> call, Throwable t) {

                        apiLiveData.setValue(AsyncResponse.errorWithObj(
                                new AddPatientResponse(false,t.getMessage(), 0L, null, null),
                                t.getMessage()));
                    }
                });

    }

    public LiveData<AsyncResponse<AddPatientResponse, Exception>> getApiLiveData() {
        if(apiLiveData == null) apiLiveData = new MutableLiveData<>(AsyncResponse.notStarted());
        return apiLiveData;
    }

    public Patient getPatient() {
        return patient;
    }
}
