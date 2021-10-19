package com.clinicapp.ui.camera.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.clinicapp.models.CameraCaptureState;
import com.clinicapp.models.CameraPositions;
import com.clinicapp.models.UploadImageResponse;
import com.clinicapp.providers.AsyncResponse;
import com.clinicapp.utilities.BaseViewModel;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody.Part;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CameraViewModel extends BaseViewModel {



    private int currentPosition;
    private List<CameraPositions> positions;
    private MutableLiveData<CameraCaptureState> captureStateLiveData;
    private MutableLiveData<AsyncResponse<Boolean,Exception>> captureCompleteLiveData;

    public CameraViewModel(@NonNull Application application) {
        super(application);
        captureCompleteLiveData = new MutableLiveData<>(AsyncResponse.notStarted());
    }



    public void init(@NonNull List<CameraPositions> positions){
        this.positions = positions;
        this.currentPosition = 0;
        final CameraCaptureState initialState = new CameraCaptureState(positions.get(0), false, null);
        captureStateLiveData = new MutableLiveData<>(initialState);
    }

    public LiveData<CameraCaptureState> getCaptureStateLiveData() {
        return captureStateLiveData;
    }
    public LiveData<AsyncResponse<Boolean, Exception>> getCaptureCompleteLiveData() {
        return captureCompleteLiveData;
    }

    public void onImageCaptured(String path) {
        updateState(positions.get(currentPosition),true, path);
    }

    public void retake() {
        updateState(positions.get(currentPosition),false, null);
    }

    private void updateState(CameraPositions position, boolean isPreview, String path){
        final CameraCaptureState state = new CameraCaptureState(position,isPreview, path);
        captureStateLiveData.setValue(state);
    }

    public void onNext(long analysisID) {
        final CameraCaptureState state = captureStateLiveData.getValue();
        File file = new File(state.getPath().replace("file://",""));
        final CameraPositions position = positions.get(currentPosition);
        Log.e( "onNext: ",file.getPath()+"" );
        String subTypeString=position.getText().toLowerCase();
        if(subTypeString.contains("zoomed")){
            subTypeString=subTypeString.replace(" (zoomed)","");
        }
        RequestBody imageBody = RequestBody.create(file,MediaType.parse("image/jpg"));
        Part imagePart = Part.createFormData("images[]", file.getName(), imageBody);
        Part mainType= Part.createFormData("mainType",position.getShootType());
        Part subType= Part.createFormData("subType",subTypeString);
//        uploadImage(mainType, subType, imagePart,analysisID);
//        
        currentPosition++;
        if (currentPosition >= positions.size()) {
            captureCompleteLiveData.setValue(AsyncResponse.success(true));
        } else {
            captureCompleteLiveData.setValue(AsyncResponse.notStarted());
            updateState(positions.get(currentPosition), false, null);
        }
    }


    private void uploadImage(Part mainType, Part subType, Part image,long analysisID) {
        captureCompleteLiveData.setValue(AsyncResponse.loading());
        repository.api.uploadImage(mainType,subType,image,analysisID)
            .enqueue(new Callback<UploadImageResponse>() {
                @Override
                public void onResponse(Call<UploadImageResponse> call, Response<UploadImageResponse> response) {
                    currentPosition++;
                    if (currentPosition >= positions.size()) {
                        captureCompleteLiveData.setValue(AsyncResponse.success(true));
                    } else {
                        captureCompleteLiveData.setValue(AsyncResponse.notStarted());
                        updateState(positions.get(currentPosition), false, null);
                    }
                }

                @Override
                public void onFailure(Call<UploadImageResponse> call, Throwable t) {
                    captureCompleteLiveData.setValue(AsyncResponse.error(t.getMessage()));
                }
            });
    }
}
