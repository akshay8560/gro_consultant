package com.clinicapp.utilities;

import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.fragment.app.DialogFragment;

public class BaseDialogFragment extends DialogFragment {
    protected static final String DATA = "DATA";

    protected void positionDialogToTop(){
        final Window window = getDialog().getWindow();
        window.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL);
        WindowManager.LayoutParams params = window.getAttributes();
        params.verticalMargin = 0.15f;
        window.setAttributes(params);
    }
}
