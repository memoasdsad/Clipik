package com.victor.clipikv2.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.FFmpegSession
import com.arthenica.ffmpegkit.ReturnCode
import com.victor.clipikv2.models.TimelineItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import kotlin.math.min

class VideoProcessor(private val context: Context) {

    companion object {
        private const val THUMBNAIL_WIDTH = 160
        private const val THUMBNAIL_HEIGHT = 90
        private const val SILENCE_THRESHOLD = -30.0 // dB
        private const val MIN_SILENCE_DURATION = 0.5 // seconds
    }

    /**
     * Generates timeline thumbnails for the video
     */
    suspend fun generateTimelineThumbnails(
        videoUri: Uri,
        durationMs: Long,
        thumbnailCount: Int
    ): List<TimelineItem> = withContext(Dispatchers.IO) {
        val timelineItems = mutableListOf<TimelineItem>()
        val retriever = MediaMetadataRetriever()

        try {
            retriever.setDataSource(context, videoUri)
            val interval = durationMs / thumbnailCount

            for (i in 0 until thumbnailCount) {
                val timeUs = i * interval * 1000 // Convert to microseconds
                val bitmap = retriever.getFrameAtTime(timeUs, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
                
                bitmap?.let {
                    val scaledBitmap = Bitmap.createScaledBitmap(it, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT, true)
                    timelineItems.add(
                        TimelineItem(
                            timeMs = i * interval,
                            thumbnail = scaledBitmap
                        )
                    )
                    it.recycle()
                }
            }
        } catch (e: Exception) {
            throw Exception("Erro ao gerar thumbnails: ${e.message}")
        } finally {
            retriever.release()
        }

        timelineItems
    }

    /**
     * Creates an automatic clip with intelligent cutting
     */
    suspend fun createAutomaticClip(
        videoUri: Uri,
        durationSeconds: Int,
        startPositionMs: Long
    ): String = withContext(Dispatchers.IO) {
        val inputPath = getVideoPath(videoUri)
        val outputPath = createTempVideoFile("temp_clip_${System.currentTimeMillis()}.mp4")

        // Find the best segment to cut
        val bestSegment = findBestSegment(inputPath, durationSeconds, startPositionMs / 1000.0)
        
        // Extract the segment
        val command = buildString {
            append("-i \"$inputPath\" ")
            append("-ss ${bestSegment.startTime} ")
            append("-t $durationSeconds ")
            append("-c:v libx264 ")
            append("-c:a aac ")
            append("-preset fast ")
            append("-crf 23 ")
            append("-y \"$outputPath\"")
        }

        val session = FFmpegKit.execute(command)
        if (!ReturnCode.isSuccess(session.returnCode)) {
            throw Exception("Erro ao cortar vídeo: ${session.failStackTrace}")
        }

        outputPath
    }

    /**
     * Converts video to vertical format (9:16)
     */
    suspend fun convertToVerticalFormat(
        inputPath: String,
        durationSeconds: Int,
        progressCallback: (Int) -> Unit = {}
    ): String = withContext(Dispatchers.IO) {
        val outputPath = createTempVideoFile("vertical_${System.currentTimeMillis()}.mp4")

        // FFmpeg command to convert to vertical format with blur background
        val command = buildString {
            append("-i \"$inputPath\" ")
            append("-filter_complex \"")
            append("[0:v]scale=1080:1920:force_original_aspect_ratio=decrease,")
            append("pad=1080:1920:(ow-iw)/2:(oh-ih)/2:black[main];")
            append("[0:v]scale=1080:1920,boxblur=20:20[bg];")
            append("[bg][main]overlay=(W-w)/2:(H-h)/2\" ")
            append("-c:v libx264 ")
            append("-c:a aac ")
            append("-preset medium ")
            append("-crf 23 ")
            append("-r 30 ")
            append("-y \"$outputPath\"")
        }

        val session = FFmpegKit.execute(command)
        
        // Monitor progress (simplified)
        var progress = 0
        while (session.state.name == "RUNNING" && progress < 100) {
            progress += 10
            progressCallback(progress)
            Thread.sleep(500)
        }

        if (!ReturnCode.isSuccess(session.returnCode)) {
            throw Exception("Erro ao converter para formato vertical: ${session.failStackTrace}")
        }

        progressCallback(100)
        outputPath
    }

    /**
     * Finds the best segment to cut based on audio analysis
     */
    private suspend fun findBestSegment(
        inputPath: String,
        durationSeconds: Int,
        preferredStartTime: Double
    ): VideoSegment = withContext(Dispatchers.IO) {
        try {
            // Get video duration
            val videoDuration = getVideoDuration(inputPath)
            
            // If preferred start time + duration fits within video, use it
            if (preferredStartTime + durationSeconds <= videoDuration) {
                return@withContext VideoSegment(preferredStartTime, durationSeconds.toDouble())
            }

            // Otherwise, find the best segment using audio analysis
            val silencePoints = detectSilencePoints(inputPath)
            val bestStart = findBestStartTime(silencePoints, durationSeconds, videoDuration)
            
            VideoSegment(bestStart, durationSeconds.toDouble())
        } catch (e: Exception) {
            // Fallback to simple cutting from the beginning
            VideoSegment(0.0, durationSeconds.toDouble())
        }
    }

    /**
     * Detects silence points in the audio for intelligent cutting
     */
    private suspend fun detectSilencePoints(inputPath: String): List<Double> = withContext(Dispatchers.IO) {
        val silencePoints = mutableListOf<Double>()
        val outputPath = createTempVideoFile("silence_analysis.txt")

        try {
            val command = buildString {
                append("-i \"$inputPath\" ")
                append("-af silencedetect=noise=${SILENCE_THRESHOLD}dB:d=$MIN_SILENCE_DURATION ")
                append("-f null - ")
                append("2> \"$outputPath\"")
            }

            val session = FFmpegKit.execute(command)
            
            if (ReturnCode.isSuccess(session.returnCode)) {
                val output = File(outputPath).readText()
                val silenceRegex = "silence_start: ([0-9.]+)".toRegex()
                
                silenceRegex.findAll(output).forEach { match ->
                    val time = match.groupValues[1].toDoubleOrNull()
                    time?.let { silencePoints.add(it) }
                }
            }
        } catch (e: Exception) {
            // Return empty list if analysis fails
        } finally {
            File(outputPath).delete()
        }

        silencePoints
    }

    /**
     * Finds the best start time based on silence points
     */
    private fun findBestStartTime(
        silencePoints: List<Double>,
        durationSeconds: Int,
        videoDuration: Double
    ): Double {
        if (silencePoints.isEmpty()) {
            return 0.0
        }

        // Find silence point that allows for full duration
        for (silencePoint in silencePoints) {
            if (silencePoint + durationSeconds <= videoDuration) {
                return silencePoint
            }
        }

        // Fallback to the latest possible start time
        return maxOf(0.0, videoDuration - durationSeconds)
    }

    /**
     * Gets video duration in seconds
     */
    private fun getVideoDuration(inputPath: String): Double {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(inputPath)
            val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            (duration?.toLongOrNull() ?: 0L) / 1000.0
        } catch (e: Exception) {
            0.0
        } finally {
            retriever.release()
        }
    }

    /**
     * Saves video to gallery
     */
    suspend fun saveToGallery(videoPath: String, fileName: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val videoFile = File(videoPath)
            if (!videoFile.exists()) return@withContext false

            val contentValues = ContentValues().apply {
                put(MediaStore.Video.Media.DISPLAY_NAME, fileName)
                put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
                put(MediaStore.Video.Media.RELATIVE_PATH, Environment.DIRECTORY_MOVIES + "/Clipik")
            }

            val uri = context.contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues)
            uri?.let { outputUri ->
                context.contentResolver.openOutputStream(outputUri)?.use { outputStream ->
                    videoFile.inputStream().use { inputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
                true
            } ?: false
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Gets the actual file path from URI
     */
    private fun getVideoPath(uri: Uri): String {
        // For simplicity, we'll use the URI directly with FFmpeg
        // In production, you might want to copy to a temporary file
        return uri.toString()
    }

    /**
     * Creates a temporary video file
     */
    private fun createTempVideoFile(fileName: String): String {
        val tempDir = File(context.cacheDir, "videos")
        if (!tempDir.exists()) {
            tempDir.mkdirs()
        }
        return File(tempDir, fileName).absolutePath
    }

    /**
     * Data class for video segments
     */
    private data class VideoSegment(
        val startTime: Double,
        val duration: Double
    )
}