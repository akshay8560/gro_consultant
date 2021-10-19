package com.clinicapp.models

import android.text.TextUtils
import com.squareup.moshi.JsonClass
import java.text.SimpleDateFormat

@JsonClass(generateAdapter = true)
class AddPatientRequest(
    val firstName:String?,
    val lastName:String?,
    val dob:String?,
    val gender:String?,
    val phone:String?
)
{

    fun toPatient(id:Long):Patient{
        return Patient(id, firstName,lastName,"","","",gender, phone,0);
    }

    companion object {
        @JvmStatic
        fun validateFirstName(firstName:String):Pair<Boolean,String?> {
            return isEmptyWithError(firstName, "First name");
        }

        @JvmStatic
        fun validateLastName(lastName:String):Pair<Boolean,String?> {
            return isEmptyWithError(lastName, "Last name");
        }

        @JvmStatic
        fun validateDateOfBirth(dob:String):Pair<Boolean,String?> {
            try {
                val format = SimpleDateFormat("MM/dd/yyyy");
                format.parse(dob)
            } catch (e:Exception){
                return Pair(false, "Invalid date.")
            }
            return isEmptyWithError(dob, "Date of birth");
        }

        @JvmStatic
        fun validateGender(dob:String):Pair<Boolean,String?> {
            return isEmptyWithError(dob, "Gender ");
        }

        @JvmStatic
        fun validatePhone(phone:String):Pair<Boolean,String?> {
            return isEmptyWithError(phone, "Phone number ");
        }

        private fun isEmptyWithError(value:String?, fieldName:String):Pair<Boolean,String?>{
            if(TextUtils.isEmpty(value)) {
                return Pair(false, "$fieldName is required.");
            }
            return Pair(true,null);
        }

    }
}
