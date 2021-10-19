package com.clinicapp.models

import android.text.TextUtils
import androidx.core.util.PatternsCompat
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequest(val email:String, val password:String){


    companion object {
        @JvmStatic
        fun validateEmail(email:String):Pair<Boolean,String> {
            if(TextUtils.isEmpty(email)) {
                return Pair(false, "Email address is required.");
            } else if(!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()){
                return Pair(false, "Please enter a valid email address.");
            }
            return Pair(true,"");
        }

        @JvmStatic
        fun validatePassword(password:String):Pair<Boolean,String> {
            if(TextUtils.isEmpty(password)) {
                return Pair(false, "Password is required.");
            } else if(password.length < 8){
                return Pair(false, "Password must be at least 8 characters.");
            }
            return Pair(true,"");
        }
    }
}
