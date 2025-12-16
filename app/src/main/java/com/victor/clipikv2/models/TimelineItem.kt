package com.victor.clipikv2.models

import android.graphics.Bitmap

/**
 * Represents a single item in the video timeline
 */
data class TimelineItem(
    val timeMs: Long,           // Time position in milliseconds
    val thumbnail: Bitmap,      // Thumbnail image for this time position
    val isSelected: Boolean = false  // Whether this item is currently selected
) {
    /**
     * Formats the time in MM:SS format
     */
    fun getFormattedTime(): String {
        val totalSeconds = timeMs / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}