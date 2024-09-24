package com.emrekizil.videocallapp.ui.call.adapter


import com.emrekizil.videocallapp.databinding.ItemUserMessageBinding
import com.emrekizil.videocallapp.ui.base.BaseViewHolder
import com.emrekizil.videocallapp.ui.call.UserMessage

class UserMessageViewHolder(
    private val binding: ItemUserMessageBinding
) : BaseViewHolder<UserMessage>(binding.root) {
    override fun bind(item: UserMessage) {
        binding.userMessageTextView.text = item.message
        binding.userUsernameMessage.text = item.username
    }
}