package com.emrekizil.videocallapp.ui.call.adapter

import com.emrekizil.videocallapp.databinding.ItemRecipientMessageBinding
import com.emrekizil.videocallapp.ui.base.BaseViewHolder
import com.emrekizil.videocallapp.ui.call.UserMessage

class RecipientMessageViewHolder (
    private val binding: ItemRecipientMessageBinding
) : BaseViewHolder<UserMessage>(binding.root){
    override fun bind(item: UserMessage) {
        binding.recipientMessageTextView.text = item.message
        binding.recipientUsernameMessage.text = item.username
    }

}