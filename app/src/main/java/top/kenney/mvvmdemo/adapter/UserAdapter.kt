package top.kenney.mvvmdemo.adapter

import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import top.kenney.mvvmdemo.R
import top.kenney.mvvmdemo.databinding.UserItemBinding
import top.kenney.mvvmdemo.model.protocol.UserModel

class UserAdapter: BaseQuickAdapter<UserModel, BaseViewHolder>(R.layout.user_item) {
    override fun convert(holder: BaseViewHolder, item: UserModel) {
        val mBinding = DataBindingUtil.getBinding<UserItemBinding>(holder.itemView)
        if(null != mBinding){
            mBinding.user = item
            mBinding.position = holder.layoutPosition
            mBinding.executePendingBindings()
        }
    }

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        DataBindingUtil.bind<UserItemBinding>(viewHolder.itemView)
    }
}