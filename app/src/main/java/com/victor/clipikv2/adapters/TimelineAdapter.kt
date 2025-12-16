package com.victor.clipikv2.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.victor.clipikv2.databinding.ItemTimelineThumbnailBinding
import com.victor.clipikv2.models.TimelineItem

/**
 * Adapter for the video timeline thumbnails
 */
class TimelineAdapter(
    private val onItemClick: (Long) -> Unit
) : ListAdapter<TimelineItem, TimelineAdapter.TimelineViewHolder>(TimelineDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineViewHolder {
        val binding = ItemTimelineThumbnailBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TimelineViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TimelineViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TimelineViewHolder(
        private val binding: ItemTimelineThumbnailBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    onItemClick(item.timeMs)
                }
            }
        }

        fun bind(item: TimelineItem) {
            binding.apply {
                // Set thumbnail image
                ivThumbnail.setImageBitmap(item.thumbnail)
                
                // Set time text
                tvTime.text = item.getFormattedTime()
                
                // Set selection state
                root.alpha = if (item.isSelected) 1.0f else 0.8f
                root.scaleX = if (item.isSelected) 1.1f else 1.0f
                root.scaleY = if (item.isSelected) 1.1f else 1.0f
            }
        }
    }

    /**
     * DiffUtil callback for efficient list updates
     */
    private class TimelineDiffCallback : DiffUtil.ItemCallback<TimelineItem>() {
        override fun areItemsTheSame(oldItem: TimelineItem, newItem: TimelineItem): Boolean {
            return oldItem.timeMs == newItem.timeMs
        }

        override fun areContentsTheSame(oldItem: TimelineItem, newItem: TimelineItem): Boolean {
            return oldItem == newItem
        }
    }

    /**
     * Updates the selection state of timeline items
     */
    fun updateSelection(selectedTimeMs: Long) {
        val updatedList = currentList.map { item ->
            item.copy(isSelected = item.timeMs == selectedTimeMs)
        }
        submitList(updatedList)
    }
}