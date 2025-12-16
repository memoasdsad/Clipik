package com.victor.clipikv2.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.victor.clipikv2.R
import com.victor.clipikv2.adapters.TimelineAdapter
import com.victor.clipikv2.databinding.ActivityEditorBinding
import com.victor.clipikv2.models.TimelineItem
import com.victor.clipikv2.utils.VideoProcessor
import kotlinx.coroutines.launch

class EditorActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_VIDEO_URI = "extra_video_uri"
    }

    private lateinit var binding: ActivityEditorBinding
    private lateinit var exoPlayer: ExoPlayer
    private lateinit var timelineAdapter: TimelineAdapter
    private lateinit var videoProcessor: VideoProcessor
    
    private var videoUri: Uri? = null
    private var videoDurationMs: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        initializePlayer()
        initializeAds()
        loadVideo()
    }

    private fun setupUI() {
        // Setup toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Setup timeline RecyclerView
        timelineAdapter = TimelineAdapter { position ->
            seekToPosition(position)
        }
        
        binding.rvTimeline.apply {
            layoutManager = LinearLayoutManager(this@EditorActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = timelineAdapter
        }

        // Setup button listeners
        binding.btnCreateStatus.setOnClickListener {
            createClip(30) // 30 seconds for Status
        }

        binding.btnCreateReels.setOnClickListener {
            createClip(60) // 60 seconds for Reels
        }

        // Initialize video processor
        videoProcessor = VideoProcessor(this)
    }

    private fun initializePlayer() {
        exoPlayer = ExoPlayer.Builder(this).build()
        binding.playerView.player = exoPlayer

        // Add player listener
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_READY -> {
                        videoDurationMs = exoPlayer.duration
                        generateTimeline()
                    }
                    Player.STATE_ENDED -> {
                        // Video ended
                    }
                }
            }
        })
    }

    private fun initializeAds() {
        MobileAds.initialize(this) {}
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }

    private fun loadVideo() {
        val videoUriString = intent.getStringExtra(EXTRA_VIDEO_URI)
        if (videoUriString != null) {
            videoUri = Uri.parse(videoUriString)
            val mediaItem = MediaItem.fromUri(videoUri!!)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
        } else {
            Toast.makeText(this, getString(R.string.error_no_video_selected), Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun generateTimeline() {
        lifecycleScope.launch {
            try {
                val timelineItems = videoProcessor.generateTimelineThumbnails(
                    videoUri!!,
                    videoDurationMs,
                    20 // Number of thumbnails
                )
                timelineAdapter.submitList(timelineItems)
            } catch (e: Exception) {
                Toast.makeText(
                    this@EditorActivity,
                    getString(R.string.error_video_process),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun seekToPosition(position: Long) {
        exoPlayer.seekTo(position)
    }

    private fun createClip(durationSeconds: Int) {
        videoUri?.let { uri ->
            showProgress(true)
            
            lifecycleScope.launch {
                try {
                    val outputPath = videoProcessor.createAutomaticClip(
                        uri,
                        durationSeconds,
                        exoPlayer.currentPosition
                    )
                    
                    showProgress(false)
                    openExportActivity(outputPath, durationSeconds)
                    
                } catch (e: Exception) {
                    showProgress(false)
                    Toast.makeText(
                        this@EditorActivity,
                        getString(R.string.error_video_process),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun showProgress(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.btnCreateStatus.isEnabled = !show
        binding.btnCreateReels.isEnabled = !show
    }

    private fun openExportActivity(outputPath: String, duration: Int) {
        val intent = Intent(this, ExportActivity::class.java).apply {
            putExtra(ExportActivity.EXTRA_OUTPUT_PATH, outputPath)
            putExtra(ExportActivity.EXTRA_DURATION, duration)
        }
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.release()
        binding.adView.destroy()
    }

    override fun onPause() {
        super.onPause()
        exoPlayer.pause()
    }
}