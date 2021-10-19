package com.clinicapp.ui.home.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.clinicapp.models.AddPatientRequest;
import com.clinicapp.models.SearchPatientsResponse;
import com.clinicapp.providers.AsyncResponse;
import com.clinicapp.utilities.BaseViewModel;

import kotlin.Pair;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchPatientViewModel extends BaseViewModel {
    private MutableLiveData<AsyncResponse<SearchPatientsResponse,Exception>> apiLiveData;

    public SearchPatientViewModel(@NonNull Application application) {
        super(application);
    }


    public Pair<Boolean, String> validatePhone(String value){
        return AddPatientRequest.validatePhone(value);
    }



    public void searchPatient(String firstName, String lastName, String phone){
        apiLiveData.postValue(AsyncResponse.loading());
        final AddPatientRequest request = new AddPatientRequest(firstName, lastName, "dob", "gender", phone);
        repository.api.searchPatient(request)
                .enqueue(new Callback<SearchPatientsResponse>() {
                    @Override
                    public void onResponse(Call<SearchPatientsResponse> call, Response<SearchPatientsResponse> response) {
                        if(response.isSuccessful()){
                            SearchPatientsResponse result = response.body();
                            if(result.getStatus()){
                                apiLiveData.setValue(AsyncResponse.success(result));
                            } else {
                                apiLiveData.setValue(AsyncResponse.error(result.getMessage()));
                            }
                        } else {
                            onFailure(call, new Exception(response.message()));
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchPatientsResponse> call, Throwable t) {
                        apiLiveData.setValue(AsyncResponse.error(t.getMessage()));
                    }
                });

    }

    public LiveData<AsyncResponse<SearchPatientsResponse, Exception>> getApiLiveData() {
        if(apiLiveData == null) apiLiveData = new MutableLiveData<>(AsyncResponse.notStarted());
        return apiLiveData;
    }
}
