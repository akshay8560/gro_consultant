package com.clinicapp.ui.camera.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.clinicapp.models.CameraPositions;
import com.clinicapp.models.HairAnalysisRequest;
import com.clinicapp.models.HairAnalysisResponse;
import com.clinicapp.providers.AsyncResponse;
import com.clinicapp.utilities.BaseViewModel;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PositionViewModel extends BaseViewModel {
    private ArrayList<CameraPositions> positions = new ArrayList<>();

    private MutableLiveData<AsyncResponse<HairAnalysisResponse,Exception>> apiLiveData;
    public PositionViewModel(@NonNull Application application) {
        super(application);
    }


    public boolean setPositions(boolean hasRight, boolean hasLeft, boolean hasFront, boolean hasBack, boolean hasTop ){
        ArrayList<Integer> selectedPositions = new ArrayList<>();

        if (hasRight) selectedPositions.add(CameraPositions.Positions.RIGHT);
        if (hasLeft) selectedPositions.add(CameraPositions.Positions.LEFT);
        if (hasFront) selectedPositions.add(CameraPositions.Positions.FRONTAL);
        if (hasBack) selectedPositions.add(CameraPositions.Positions.VERTEX);
        if (hasTop) selectedPositions.add(CameraPositions.Positions.CROWN);

        Collections.sort(selectedPositions);
        positions = CameraPositions.getCameraPositionArray(selectedPositions);
        return !selectedPositions.isEmpty();
    }

    public boolean setZoomPositions(boolean hasCrownTop, boolean hasCrownMid, boolean hasCrownBottom,
                                    boolean hasHairRight, boolean hasHairBack, boolean hasHairLeft) {
        ArrayList<Integer> selectedPositions = new ArrayList<>();

        if(hasHairRight) {
            selectedPositions.add(CameraPositions.Positions.HAIR_RIGHT);
            selectedPositions.add(CameraPositions.Positions.HAIR_RIGHT_ZOOM);
        }
        if(hasHairLeft) {
            selectedPositions.add(CameraPositions.Positions.HAIR_LEFT);
            selectedPositions.add(CameraPositions.Positions.HAIR_LEFT_ZOOM);
        }
        if(hasHairBack) {
            selectedPositions.add(CameraPositions.Positions.HAIR_OCCIPITAL);
            selectedPositions.add(CameraPositions.Positions.HAIR_OCCIPITAL_ZOOM);
        }
        if(hasCrownTop) {
            selectedPositions.add(CameraPositions.Positions.HAIR_FRONTAL);
            selectedPositions.add(CameraPositions.Positions.HAIR_FRONTAL_ZOOM);
        }
        if(hasCrownMid) {
            selectedPositions.add(CameraPositions.Positions.HAIR_CROWN);
            selectedPositions.add(CameraPositions.Positions.HAIR_CROWN_ZOOM);
        }
        if(hasCrownBottom) {
            selectedPositions.add(CameraPositions.Positions.HAIR_VERTEX);
            selectedPositions.add(CameraPositions.Positions.HAIR_VERTEX_ZOOM);
        }
        Collections.sort(selectedPositions);
        positions = CameraPositions.getCameraPositionArray(selectedPositions);
        return !selectedPositions.isEmpty();
    }

    public ArrayList<CameraPositions> getSelectedPositions() {
        return positions;
    }

    public void createAnalysisID(long patientId){
        apiLiveData.postValue(AsyncResponse.loading());

        final HairAnalysisRequest request=new HairAnalysisRequest(patientId);

        repository.api.createHairAnalysis(request)
                .enqueue(new Callback<HairAnalysisResponse>() {
                    @Override
                    public void onResponse(Call<HairAnalysisResponse> call, Response<HairAnalysisResponse> response) {
                        if(response.isSuccessful()){
                            HairAnalysisResponse result=response.body();
                            apiLiveData.setValue(AsyncResponse.success(result));
                        }else {
                            onFailure(call, new Exception(response.message()));
                        }
                    }

                    @Override
                    public void onFailure(Call<HairAnalysisResponse> call, Throwable t) {
                        apiLiveData.setValue(AsyncResponse.error(t.getMessage()));
                    }
                });
    }


    public LiveData<AsyncResponse<HairAnalysisResponse, Exception>> getApiLiveData() {
        if(apiLiveData == null) apiLiveData = new MutableLiveData<>(AsyncResponse.notStarted());
        return apiLiveData;
    }
}