package com.emrekizil.videocallapp.ui.profile.adapter

import com.emrekizil.videocallapp.data.database.CallHistoryEntity
import com.emrekizil.videocallapp.databinding.ItemCallHistoryBinding
import com.emrekizil.videocallapp.ui.base.BaseViewHolder
import java.util.concurrent.TimeUnit

class CallHistoryViewHolder (
    private val binding:ItemCallHistoryBinding,
    private val onCallHistoryItemClickListener:((String)->Unit)?
):BaseViewHolder<CallHistoryEntity>(binding.root) {


    override fun bind(item: CallHistoryEntity) {
        with(binding){
            recipientUsernameTextView.text = item.username
            timestampTextView.text = getTimeAgo(item.timestamp)
            root.setOnClickListener {
                onCallHistoryItemClickListener?.invoke(item.username)
            }
        }
    }

    private fun getTimeAgo(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp

        val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
        val hours = TimeUnit.MILLISECONDS.toHours(diff)
        val days = TimeUnit.MILLISECONDS.toDays(diff)

        return when {
            minutes < 60 -> "$minutes minutes ago"
            hours < 24 -> "$hours hours ago"
            else -> "$days days ago"
        }
    }
}