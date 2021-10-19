package com.clinicapp.ui.camera;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.clinicapp.R;
import com.clinicapp.databinding.FragmentCameraBinding;
import com.clinicapp.models.CameraCaptureState;
import com.clinicapp.models.CameraPositions;
import com.clinicapp.models.Patient;
import com.clinicapp.providers.AsyncResponse;
import com.clinicapp.ui.camera.viewmodel.CameraViewModel;
import com.clinicapp.ui.home.HomeFragment;
import com.clinicapp.ui.home.viewmodels.MainViewModel;
import com.clinicapp.utilities.BaseFragment;
import com.clinicapp.utilities.CameraUtils;
import com.clinicapp.utilities.Utils;

public class CameraFragment extends BaseFragment {
    private FragmentCameraBinding views;

    private CameraViewModel viewModel;
    private MainViewModel mainViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        views = FragmentCameraBinding.inflate(getLayoutInflater(),container,false);

        return views.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(CameraViewModel.class);
        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        viewModel.init(mainViewModel.getSelectedPositions());
        initViews();
        initListeners();
    }

    private void initViews() {
        views.groupCapture.setVisibility(View.VISIBLE);
        views.groupPreview.setVisibility(View.INVISIBLE);
    }

    private void initListeners(){
        views.captureImage.setOnClickListener(view -> takePhoto());
        views.btCancel.setOnClickListener(this::onCancelShoot);
        views.btRetake.setOnClickListener(view -> viewModel.retake());
        views.btNext.setOnClickListener(view->viewModel.onNext(mainViewModel.getHairAnalysisID()));
        viewModel.init(mainViewModel.getSelectedPositions());
        viewModel.getCaptureStateLiveData().observe(getViewLifecycleOwner(),this::handleCaptureStateChange);
        viewModel.getCaptureCompleteLiveData().observe(getViewLifecycleOwner(),this::handleCaptureUploadChange);
    }

    private void handleCaptureUploadChange(AsyncResponse<Boolean, Exception> response) {
        if(response.isFresh()){
            response.pop();
            if(response.isNotStarted()){
                views.btNext.setVisibility(View.INVISIBLE);
                views.progressBar.setVisibility(View.GONE);
            } else if(response.isError()){
                Toast.makeText(getContext(),response.error.getMessage(),Toast.LENGTH_LONG).show();
                views.btNext.setVisibility(View.VISIBLE);
                views.progressBar.setVisibility(View.GONE);
            } else if(response.isLoading()){
                views.btNext.setVisibility(View.INVISIBLE);
                views.progressBar.setVisibility(View.VISIBLE);
            } else if (response.isSuccess()){
                Navigation.findNavController(views.getRoot())
                        .navigate(R.id.action_cameraFragment_to_returnToHomeScreen, getArguments());
            }
        }
    }

    private void onCancelShoot(View view) {
        Navigation.findNavController(view)
                .navigateUp();
    }

    private void handleCaptureStateChange(CameraCaptureState state) {
        Patient patient = mainViewModel.getSelectedPatient();
        patient = new Patient(1,"Test","LastTest","ab","20"
                ,"01-01-2000","Male","123456",29  );

        final CameraPositions position = state.getPosition();
        toggleCapturedPreview(state.isPreview());
        setMarkerState(position);

        //For Camera Frame size and zoomed text visibility
        if (position.isHairPosition()) {
            views.previewView.setVisibility(View.GONE);
            //For zoomed text visibility
            if (position.zoom > 0) {
                views.zoomedText.setVisibility(View.VISIBLE);
            } else{
                views.zoomedText.setVisibility(View.INVISIBLE);
            }
        } else {
            views.previewView.setVisibility(View.VISIBLE);
        }

        //For Camera Overlay
        if(state.isPreview()) {
            Glide.with(this)
                .load(state.getPath())
                .into(views.imgCapturePreview);
        } else {
            views.txtPositionName.setText(position.getText());
            Glide.with(this)
                .load(position.getImage(!patient.isFemale()))
                .into(position.isHairPosition() ? views.hairImgPosition:views.imgPosition); //if position is hair position show image on hairImgPosition View or else imgPosition View

            Glide.with(this)
                    .load(setOverlay(position))
                    .into(views.imgOverlay);

            startCamera(position.zoom);
        }
    }
    private int setOverlay(CameraPositions position){
        int overlay=position.getOverlay();
        if(overlay==R.drawable.hair_camera_overlay){    //handling hair shoot overlay
            views.imgHairOverlay.setVisibility(View.VISIBLE);
            views.imgOverlay.setVisibility(View.INVISIBLE);
            views.imgCrownOverlay.setVisibility(View.INVISIBLE);
        }else if(overlay==R.drawable.crown_camera_overlay){      //positioning and handling crown shoot overlay
            views.imgCrownOverlay.setVisibility(View.VISIBLE);
            views.imgOverlay.setVisibility(View.INVISIBLE);
            views.imgHairOverlay.setVisibility(View.INVISIBLE);
        }else{
            views.imgCrownOverlay.setVisibility(View.INVISIBLE);
            views.imgHairOverlay.setVisibility(View.INVISIBLE);
            views.imgOverlay.setVisibility(View.VISIBLE);
        }
        return overlay;
    }

    private void setMarkerState(CameraPositions position) {
        boolean isVisible = position.shouldShowMarker();
        views.imgPosition.setVisibility(!position.isHairPosition()?View.VISIBLE:View.INVISIBLE);
        views.hairImgPosition.setVisibility(position.isHairPosition()?View.VISIBLE:View.INVISIBLE);

        views.cbVertex.setVisibility(position.isHeadVisible() ? View.VISIBLE:View.GONE);
        views.cbCrown.setVisibility(position.isHeadVisible() ? View.VISIBLE:View.GONE);
        views.cbFrontal.setVisibility(isVisible ? View.VISIBLE:View.GONE);
        views.cbLeft.setVisibility(position.isLeftHeadVisible() ? View.VISIBLE:View.GONE);
        views.cbRight.setVisibility(position.isRightHeadVisible() ? View.VISIBLE:View.GONE);

        views.cbFrontal.setChecked(position.isFrontalChecked());
        views.cbVertex.setChecked(position.isVertexChecked());
        views.cbCrown.setChecked(position.isCrownChecked());
    }

    private void toggleCapturedPreview(boolean showCapturedImage){
        //remove hair overlay, because we are making it visible in handleCaptureState anyway (if needed).
        views.imgHairOverlay.setVisibility(View.GONE);
        views.groupPreview.setVisibility(showCapturedImage ? View.VISIBLE:View.GONE);
        views.groupCapture.setVisibility(showCapturedImage ? View.INVISIBLE:View.VISIBLE);
        if(showCapturedImage)
            CameraUtils.Companion.getGetCameraProvider().unbindAll();
        views.captureImage.setClickable(!showCapturedImage);
        views.btNext.setVisibility(showCapturedImage ? View.VISIBLE:View.GONE);
    }


    private void startCamera(float zoom){
        if(Utils.checkCameraPermission(getContext())){
            //Start Camera after checking the permission
            CameraUtils.Companion.startCamera(getContext(),getViewLifecycleOwner(), views.preview,zoom);
        }else{
            Utils.notify(getContext(),"Unable to access Camera");
            getFragmentManager().beginTransaction().replace(R.id.main_layout,new HomeFragment()).commit();
        }
    }



    private void takePhoto() {
        CameraUtils.Companion.takePhoto(getContext()).observe(getViewLifecycleOwner(), viewModel::onImageCaptured);
    }




}