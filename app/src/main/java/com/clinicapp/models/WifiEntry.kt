package com.clinicapp.models

import android.net.wifi.ScanResult
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WifiEntry(
    val details:ScanResult,
    val isConnected:Boolean
) : Parcelable