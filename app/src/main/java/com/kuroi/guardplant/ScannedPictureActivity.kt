package com.kuroi.guardplant

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Base64
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.kuroi.guardplant.databinding.ActivityScannedPictureBinding
import java.io.ByteArrayOutputStream

class ScannedPictureActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityScannedPictureBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScannedPictureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val receivedIntent = intent

        if (receivedIntent != null && receivedIntent.hasExtra("plantImage")) {
            val plantImage: Bitmap? = receivedIntent.getParcelableExtra("plantImage")!!
            val plantBase64Img = bitmapToBase64(plantImage!!)

            binding.ivScannedPicture.setImageBitmap(plantImage)
            

        }


    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

}