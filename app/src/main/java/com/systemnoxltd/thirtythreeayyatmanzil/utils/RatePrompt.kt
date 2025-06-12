package com.systemnoxltd.thirtythreeayyatmanzil.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AlertDialog

object RatePrompt {

    private const val PREFS_NAME = "rate_prompt_prefs"
    private const val LAUNCH_COUNT = "launch_count"
    private const val PROMPT_INTERVAL = 5 // show every 5th time

    fun checkAndPrompt(context: Context, onDecline: () -> Unit): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val launchCount = prefs.getInt(LAUNCH_COUNT, 0) + 1
        prefs.edit().putInt(LAUNCH_COUNT, launchCount).apply()

        return if (launchCount % PROMPT_INTERVAL == 0) {
            showPromptDialog(context, onDecline)
            true
        } else {
            onDecline() // Close app directly
            false
        }
    }

    private fun showPromptDialog(context: Context, onDecline: () -> Unit) {
        AlertDialog.Builder(context)
            .setTitle("Rate Us")
            .setMessage("If you enjoy using the app, please rate it!")
            .setPositiveButton("Rate Now") { _, _ ->
                rateOnPlayStore(context)
            }
            .setNegativeButton("No Thanks") { _, _ ->
                onDecline() // Exit app if user says "No Thanks"
            }
            .setCancelable(true)
            .show()
    }

    fun rateOnPlayStore(context: Context) {
        val uri = Uri.parse("market://details?id=${context.packageName}")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(intent)
    }
}
