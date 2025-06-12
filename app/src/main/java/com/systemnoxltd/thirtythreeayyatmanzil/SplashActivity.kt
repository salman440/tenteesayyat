package com.systemnoxltd.thirtythreeayyatmanzil

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.systemnoxltd.thirtythreeayyatmanzil.utils.UpdateChecker

class SplashActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_splash)
////        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
////            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
////            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
////            insets
////        }
//
//        startActivity(Intent(this, MainActivity::class.java))
//        finish()
//
//    }

    private val splashDuration = 2000L // minimum 2 seconds
    private var updateCheckDone = false
    private var splashDelayDone = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val logo = findViewById<ImageView>(R.id.logoImageView)
        val animation = AnimationUtils.loadAnimation(this, R.anim.logo_animation)
        logo.startAnimation(animation)

        // 1. Start update check
        UpdateChecker.checkForUpdate(this) {
            updateCheckDone = true
            tryProceed()
        }

        // 2. Minimum splash delay
        Handler(Looper.getMainLooper()).postDelayed({
            splashDelayDone = true
            tryProceed()
        }, splashDuration)
    }

    private fun tryProceed() {
        if (updateCheckDone && splashDelayDone) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}