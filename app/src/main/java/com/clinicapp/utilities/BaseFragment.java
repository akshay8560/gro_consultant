package com.clinicapp.utilities;

import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import kotlin.Pair;

public class BaseFragment extends Fragment {

    protected static final String DATA = "DATA";

    protected boolean handleValidation(TextView error, Pair<Boolean,String> validationResult){
        error.setVisibility(validationResult.getFirst() ? View.INVISIBLE:View.VISIBLE);
        error.setText(validationResult.getSecond());
        return validationResult.getFirst();
    }

    protected void setViewState(boolean isEnabled, View...views){
        for (View view:views) {
            view.setEnabled(isEnabled);
        }
    }

    protected void showError(TextView errorView, String value){
        errorView.setVisibility(View.VISIBLE);
        errorView.setText(value);
    }
}
