package com.victor.clipikv2.activities

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.victor.clipikv2.R
import com.victor.clipikv2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Video picker launcher
    private val videoPickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.data?.let { videoUri ->
                openEditor(videoUri)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        initializeAds()
    }

    private fun setupUI() {
        // Set click listeners
        binding.btnSelectVideo.setOnClickListener {
            checkPermissionsAndSelectVideo()
        }

        binding.cardSelectVideo.setOnClickListener {
            checkPermissionsAndSelectVideo()
        }
    }

    private fun initializeAds() {
        // Initialize Mobile Ads SDK
        MobileAds.initialize(this) {}

        // Load banner ad
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }

    private fun checkPermissionsAndSelectVideo() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_VIDEO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        Dexter.withContext(this)
            .withPermission(permission)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    selectVideoFromGallery()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.permission_denied),
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    private fun selectVideoFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI).apply {
            type = "video/*"
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("video/mp4", "video/avi", "video/mov", "video/mkv"))
        }

        try {
            videoPickerLauncher.launch(intent)
        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.error_video_load), Toast.LENGTH_SHORT).show()
        }
    }

    private fun openEditor(videoUri: Uri) {
        val intent = Intent(this, EditorActivity::class.java).apply {
            putExtra(EditorActivity.EXTRA_VIDEO_URI, videoUri.toString())
        }
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.adView.destroy()
    }
}