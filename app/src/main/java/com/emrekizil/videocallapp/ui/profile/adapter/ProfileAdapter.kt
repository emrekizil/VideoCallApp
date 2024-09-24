package com.emrekizil.videocallapp.ui.profile.adapter

import android.view.ViewGroup
import com.emrekizil.videocallapp.data.database.CallHistoryEntity
import com.emrekizil.videocallapp.databinding.ItemCallHistoryBinding
import com.emrekizil.videocallapp.ui.base.BaseAdapter
import com.emrekizil.videocallapp.ui.base.BaseViewHolder
import com.emrekizil.videocallapp.utils.inflateAdapterItem

class ProfileAdapter : BaseAdapter<CallHistoryEntity, BaseViewHolder<CallHistoryEntity>>() {

    private var onCallItemClickListener: ((String) -> Unit)? = null

    fun setOnCallItemClickListener(onCallItemClickListener: ((String) -> Unit)) {
        this.onCallItemClickListener = onCallItemClickListener
    }

    override fun onCreateHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<CallHistoryEntity> {
        return CallHistoryViewHolder(
            parent.inflateAdapterItem(ItemCallHistoryBinding::inflate),
            onCallItemClickListener
        )
    }

    override fun findItemViewType(position: Int): Int {
        return 1
    }
}