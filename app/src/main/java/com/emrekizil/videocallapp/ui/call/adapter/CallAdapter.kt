package com.emrekizil.videocallapp.ui.call.adapter

import android.view.ViewGroup
import com.emrekizil.videocallapp.databinding.ItemRecipientMessageBinding
import com.emrekizil.videocallapp.databinding.ItemUserMessageBinding
import com.emrekizil.videocallapp.ui.base.BaseAdapter
import com.emrekizil.videocallapp.ui.base.BaseViewHolder
import com.emrekizil.videocallapp.ui.call.MessageType
import com.emrekizil.videocallapp.ui.call.UserMessage
import com.emrekizil.videocallapp.utils.inflateAdapterItem

class CallAdapter : BaseAdapter<UserMessage, BaseViewHolder<UserMessage>>() {

    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<UserMessage> {
        return when (viewType) {
            USER_MESSAGE_ITEM_VIEW_TYPE -> {
                UserMessageViewHolder(
                    parent.inflateAdapterItem(ItemUserMessageBinding::inflate)
                )
            }

            RECIPIENT_MESSAGE_VIEW_TYPE -> {
                RecipientMessageViewHolder(
                    parent.inflateAdapterItem(ItemRecipientMessageBinding::inflate)
                )
            }

            else -> throw Exception("Can not be constructed view holder with type:$viewType")
        }
    }

    override fun findItemViewType(position: Int): Int {
        return when (getItemAtPosition(position)?.messageType) {
            MessageType.USER -> USER_MESSAGE_ITEM_VIEW_TYPE
            MessageType.RECIPIENT -> RECIPIENT_MESSAGE_VIEW_TYPE
            null -> INVALID_VIEW_TYPE
        }
    }

    companion object {
        private const val USER_MESSAGE_ITEM_VIEW_TYPE = 0
        private const val RECIPIENT_MESSAGE_VIEW_TYPE = 1
        private const val INVALID_VIEW_TYPE = -1
    }
}