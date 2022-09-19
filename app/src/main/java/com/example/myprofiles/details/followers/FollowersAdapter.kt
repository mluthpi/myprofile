package com.example.myprofiles.details.followers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myprofiles.User
import com.example.myprofiles.databinding.ListUserBinding
import com.example.myprofiles.model.UserDetailsResponse

class FollowersAdapter: RecyclerView.Adapter<FollowersAdapter.ViewHolder>() {

    private val userItemList = mutableListOf<UserDetailsResponse>()

    fun addItems(userItemList: UserDetailsResponse) {
        this.userItemList.clear()
        this.userItemList.addAll(listOf(userItemList))
        notifyDataSetChanged()
    }


    class ViewHolder(private val binding : ListUserBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(userItem: UserDetailsResponse) {
            with(binding) {
                Glide.with(binding.root)
                    .load(userItem.followers)
                    .into(imgAvatar)
                tvName.text = userItem.followersUrl
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val listUserBinding = ListUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(listUserBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userItem = userItemList[position]
        holder.bind(userItem)
    }

    override fun getItemCount(): Int = userItemList.size

}