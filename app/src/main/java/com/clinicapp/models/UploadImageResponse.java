package com.clinicapp.models;

import com.squareup.moshi.Json;

import java.util.List;

public class UploadImageResponse {
    @Json(name="status")
    boolean status;
    @Json(name="image_ids")
    List imageId;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List getImageId() {
        return imageId;
    }

    public void setImageId(List imageId) {
        this.imageId = imageId;
    }
}
