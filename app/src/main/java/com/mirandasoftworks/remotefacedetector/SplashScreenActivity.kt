package com.mirandasoftworks.remotefacedetector

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.mirandasoftworks.remotefacedetector.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.ivSplashScreen.alpha = 0f
        binding.ivSplashScreen.animate().setDuration(1500).alpha(1f).withEndAction{
            if (getSharedPreferences("PREFERENCES", MODE_PRIVATE).getBoolean("loginState", true)){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
                finish()
            } else{
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
                finish()
            }

        }

//        binding.ivSplashScreen.alpha = 0f
//        binding.ivSplashScreen.animate().setDuration(1500).alpha(1f).withEndAction{
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
//            overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
//            finish()
//        }
    }
}