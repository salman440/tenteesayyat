package com.systemnoxltd.thirtythreeayyatmanzil.utils

import android.app.Activity
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability

//object UpdateChecker {
//    fun checkForUpdate(activity: Activity) {
//        val appUpdateManager = AppUpdateManagerFactory.create(activity)
//        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
//
//        appUpdateInfoTask.addOnSuccessListener { info ->
//            if (info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
//                info.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
//                appUpdateManager.startUpdateFlowForResult(
//                    info, AppUpdateType.IMMEDIATE, activity, 123
//                )
//            }
//        }
//    }
//}


object UpdateChecker {
    fun checkForUpdate(activity: Activity, onCheckComplete: () -> Unit) {
        val appUpdateManager = AppUpdateManagerFactory.create(activity)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { info ->
            if (info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                info.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                appUpdateManager.startUpdateFlowForResult(
                    info, AppUpdateType.IMMEDIATE, activity, 123
                )
            } else {
                // No update available or not allowed → proceed
                onCheckComplete()
            }
        }.addOnFailureListener {
            // Update check failed → proceed
            onCheckComplete()
        }
    }
}