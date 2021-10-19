package com.clinicapp.utilities;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class PostTextWatcher implements TextWatcher {

    final EditText editText;
    private PostTextChangeListener listener;

    public PostTextWatcher(EditText editText, PostTextChangeListener listener){
        this.editText = editText;
        this.listener = listener;
        editText.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        this.listener.onChange(s.toString());
    }

    public static interface PostTextChangeListener {
        void onChange(String value);
    }
}
