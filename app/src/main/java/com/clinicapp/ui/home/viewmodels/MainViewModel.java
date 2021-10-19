package com.clinicapp.ui.home.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;

import com.clinicapp.models.CameraPositions;
import com.clinicapp.models.Patient;
import com.clinicapp.utilities.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends BaseViewModel {

    private List<Patient> searchResults = new ArrayList<>();
    private Patient selectedPatient;
    private long hairAnalysisID;
    private ArrayList<CameraPositions> selectedPositions = new ArrayList<>();

    public long getHairAnalysisID() {
        return hairAnalysisID;
    }

    public void setHairAnalysisID(long hairAnalysisID) {
        this.hairAnalysisID = hairAnalysisID;
    }

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public void onPatientAdded(Patient patient){
        this.selectedPatient = patient;
    }

    public void setSearchResult(List<Patient> patients){
        this.searchResults = patients;
    }

    public List<Patient> getSearchResults() {
        return searchResults;
    }

    public Patient getSelectedPatient() {
        return selectedPatient;
    }

    public void setSelectedPatient(Patient selectedPatient) {
        this.selectedPatient = selectedPatient;
    }

    public void setCameraPositions(ArrayList<CameraPositions> selectedPositions) {
        this.selectedPositions = selectedPositions;
    }

    public ArrayList<CameraPositions> getSelectedPositions() {
        return selectedPositions;
    }
}
