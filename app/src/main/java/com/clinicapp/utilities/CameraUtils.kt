package com.clinicapp.utilities

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.clinicapp.R
import java.io.File

class CameraUtils {

    companion object{
        var imageCapture:ImageCapture?=null
        var camera:Camera?=null
        var preview:Preview?=null
        var getCameraProvider:ProcessCameraProvider?=null
        public fun startCamera(context:Context, lyfCycle:LifecycleOwner, previewView:PreviewView, zoom:Float){
            val cameraProviderFuture= ProcessCameraProvider.getInstance(context)


            cameraProviderFuture.addListener({

                val cameraProvider=cameraProviderFuture.get()
                preview= Preview.Builder()
                        .build()
                        .also { mPreview->
                            mPreview.setSurfaceProvider(previewView.surfaceProvider)
                        }

                imageCapture=ImageCapture.Builder()
                        .build()


                val cameraSelector=CameraSelector.DEFAULT_BACK_CAMERA

                try {


                    cameraProvider.unbindAll()

                    camera=cameraProvider.bindToLifecycle(lyfCycle, cameraSelector,preview,imageCapture)
                    camera!!.cameraControl.setZoomRatio(zoom)
                    getCameraProvider=cameraProvider
                    

                }catch (e:Exception){
                    Log.d("CameraAPI","StartCamera Fail: ",e)

                }


            },ContextCompat.getMainExecutor(context))
        }



        fun getOutputDirectory(context: Context):File{
            val mediaDir=context.externalCacheDirs.firstOrNull()?.let { mFile->
                File(mFile,context.resources.getString(R.string.app_name)).apply {
                    mkdirs()
                }
            }

            return if(mediaDir!=null&&mediaDir.exists())
                mediaDir else context.filesDir
        }
        fun takePhoto(context: Context):LiveData<String>{
            val liveData = MutableLiveData<String>()
            val imageCapture= imageCapture?:return liveData
            val photoFile= File(getOutputDirectory(context),
                    "${R.string.app_name}-${System.currentTimeMillis()}.jpg")
            val outputOption=ImageCapture.OutputFileOptions
                    .Builder(photoFile)
                    .build()

            val takePic=imageCapture.takePicture(outputOption,ContextCompat.getMainExecutor(context),
                object :ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        val savedUri= Uri.fromFile(photoFile)
                        liveData.postValue(savedUri.toString())
                    }

                    override fun onError(exception: ImageCaptureException) {
                        liveData.postValue(exception.toString())
                        Log.e("CameraAPI","",exception)
                    }
                }
            )

            return liveData

        }
    }
}