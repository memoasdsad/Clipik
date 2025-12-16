package com.victor.clipikv2.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.InterstitialAdLoadCallback
import com.google.android.gms.ads.LoadAdError
import com.victor.clipikv2.R
import com.victor.clipikv2.databinding.ActivityExportBinding
import com.victor.clipikv2.utils.VideoProcessor
import kotlinx.coroutines.launch
import java.io.File

class ExportActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_OUTPUT_PATH = "extra_output_path"
        const val EXTRA_DURATION = "extra_duration"
    }

    private lateinit var binding: ActivityExportBinding
    private lateinit var videoProcessor: VideoProcessor
    private var interstitialAd: InterstitialAd? = null
    
    private var outputPath: String? = null
    private var duration: Int = 0
    private var finalVideoPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        loadInterstitialAd()
        startExport()
    }

    private fun setupUI() {
        outputPath = intent.getStringExtra(EXTRA_OUTPUT_PATH)
        duration = intent.getIntExtra(EXTRA_DURATION, 30)

        binding.btnCancel.setOnClickListener {
            finish()
        }

        binding.btnShare.setOnClickListener {
            shareVideo()
        }

        binding.btnSave.setOnClickListener {
            saveToGallery()
        }

        binding.btnBackToEditor.setOnClickListener {
            finish()
        }

        videoProcessor = VideoProcessor(this)
    }

    private fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            this,
            getString(R.string.ad_interstitial_unit_id),
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitialAd = null
                }
            }
        )
    }

    private fun startExport() {
        outputPath?.let { path ->
            lifecycleScope.launch {
                try {
                    updateProgress(0, "Iniciando processamento...")
                    
                    // Convert to vertical format (9:16)
                    updateProgress(25, "Convertendo para formato vertical...")
                    finalVideoPath = videoProcessor.convertToVerticalFormat(
                        path,
                        duration
                    ) { progress ->
                        runOnUiThread {
                            updateProgress(25 + (progress * 0.75).toInt(), "Processando vídeo...")
                        }
                    }
                    
                    updateProgress(100, "Concluído!")
                    showSuccess()
                    showInterstitialAd()
                    
                } catch (e: Exception) {
                    showError(e.message ?: "Erro desconhecido")
                }
            }
        } ?: run {
            showError("Caminho do vídeo não encontrado")
        }
    }

    private fun updateProgress(progress: Int, status: String) {
        binding.progressBar.progress = progress
        binding.tvProgress.text = "$progress%"
        binding.tvExportStatus.text = status
    }

    private fun showSuccess() {
        binding.tvExportTitle.text = getString(R.string.export_complete)
        binding.tvExportTitle.setTextColor(getColor(R.color.accent_green))
        binding.progressBar.visibility = View.GONE
        binding.tvProgress.visibility = View.GONE
        binding.tvExportStatus.visibility = View.GONE
        binding.btnCancel.visibility = View.GONE
        binding.successContainer.visibility = View.VISIBLE
    }

    private fun showError(message: String) {
        binding.tvExportTitle.text = getString(R.string.export_failed)
        binding.tvExportTitle.setTextColor(getColor(R.color.accent_red))
        binding.tvExportStatus.text = message
        binding.progressBar.visibility = View.GONE
        binding.tvProgress.visibility = View.GONE
        
        binding.btnCancel.text = getString(R.string.retry)
        binding.btnCancel.setOnClickListener {
            startExport()
        }
    }

    private fun showInterstitialAd() {
        interstitialAd?.show(this)
    }

    private fun shareVideo() {
        finalVideoPath?.let { path ->
            try {
                val file = File(path)
                val uri = FileProvider.getUriForFile(
                    this,
                    "${packageName}.fileprovider",
                    file
                )

                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "video/mp4"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }

                startActivity(Intent.createChooser(shareIntent, getString(R.string.share_video)))
            } catch (e: Exception) {
                Toast.makeText(this, "Erro ao compartilhar vídeo", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveToGallery() {
        finalVideoPath?.let { path ->
            lifecycleScope.launch {
                try {
                    val success = videoProcessor.saveToGallery(path, "Clipik_${System.currentTimeMillis()}.mp4")
                    if (success) {
                        Toast.makeText(
                            this@ExportActivity,
                            getString(R.string.save_to_gallery),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@ExportActivity,
                            "Erro ao salvar na galeria",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        this@ExportActivity,
                        "Erro ao salvar: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Clean up temporary files
        outputPath?.let { path ->
            try {
                File(path).delete()
            } catch (e: Exception) {
                // Ignore cleanup errors
            }
        }
    }
}