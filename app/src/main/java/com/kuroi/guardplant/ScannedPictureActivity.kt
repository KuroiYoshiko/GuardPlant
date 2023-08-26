package com.kuroi.guardplant

import android.graphics.Bitmap
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.kuroi.guardplant.databinding.ActivityScannedPictureBinding

class ScannedPictureActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityScannedPictureBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScannedPictureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val receivedIntent = intent
        if (receivedIntent != null && receivedIntent.hasExtra("plantImage")) {
            val plantImage: Bitmap? = receivedIntent.getParcelableExtra("plantImage")
            binding.ivScannedPicture.setImageBitmap(plantImage)
        }

    }

}