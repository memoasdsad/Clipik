package com.victor.clipikv2.utils

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Utility class for file operations
 */
object FileUtils {

    /**
     * Gets the display name of a file from its URI
     */
    fun getFileName(context: Context, uri: Uri): String? {
        var fileName: String? = null
        
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val nameIndex = it.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME)
                    if (nameIndex >= 0) {
                        fileName = it.getString(nameIndex)
                    }
                }
            }
        }
        
        if (fileName == null) {
            fileName = uri.path?.let { path ->
                val cut = path.lastIndexOf('/')
                if (cut != -1) path.substring(cut + 1) else path
            }
        }
        
        return fileName
    }

    /**
     * Gets the file size from URI
     */
    fun getFileSize(context: Context, uri: Uri): Long {
        var size = 0L
        
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val sizeIndex = it.getColumnIndex(MediaStore.Video.Media.SIZE)
                    if (sizeIndex >= 0) {
                        size = it.getLong(sizeIndex)
                    }
                }
            }
        } else if (uri.scheme == "file") {
            uri.path?.let { path ->
                val file = File(path)
                if (file.exists()) {
                    size = file.length()
                }
            }
        }
        
        return size
    }

    /**
     * Formats file size in human readable format
     */
    fun formatFileSize(bytes: Long): String {
        val kb = 1024
        val mb = kb * 1024
        val gb = mb * 1024

        return when {
            bytes >= gb -> String.format("%.1f GB", bytes.toFloat() / gb)
            bytes >= mb -> String.format("%.1f MB", bytes.toFloat() / mb)
            bytes >= kb -> String.format("%.1f KB", bytes.toFloat() / kb)
            else -> "$bytes B"
        }
    }

    /**
     * Creates a unique filename with timestamp
     */
    fun createUniqueFileName(prefix: String, extension: String): String {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        return "${prefix}_${timestamp}.$extension"
    }

    /**
     * Cleans up temporary files in the cache directory
     */
    fun cleanupTempFiles(context: Context) {
        try {
            val tempDir = File(context.cacheDir, "videos")
            if (tempDir.exists()) {
                tempDir.listFiles()?.forEach { file ->
                    if (file.isFile && file.lastModified() < System.currentTimeMillis() - 24 * 60 * 60 * 1000) {
                        file.delete()
                    }
                }
            }
        } catch (e: Exception) {
            // Ignore cleanup errors
        }
    }

    /**
     * Checks if there's enough storage space for video processing
     */
    fun hasEnoughStorage(context: Context, requiredBytes: Long): Boolean {
        return try {
            val availableBytes = context.cacheDir.freeSpace
            availableBytes > requiredBytes * 2 // Require 2x space for safety
        } catch (e: Exception) {
            false
        }
    }
}