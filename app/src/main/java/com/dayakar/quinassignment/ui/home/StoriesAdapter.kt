package com.dayakar.quinassignment.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.dayakar.quinassignment.R
import com.dayakar.quinassignment.databinding.StorySingleItemBinding
import com.dayakar.quinassignment.model.Story

/*
* Created By DAYAKAR GOUD BANDARI on 03-04-2021.
*/
class StoriesAdapter:ListAdapter<Story, StoriesAdapter.StoryViewHolder>(StoryDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        return StoryViewHolder(StorySingleItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val currentStory=getItem(position)
        holder.bind(currentStory)
    }

    inner class StoryViewHolder(val binding:StorySingleItemBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(story:Story){

            binding.postUserName.text=story.userName
            binding.postTaggedLocation.text=story.taggedLocation

            binding.storyDetails.text=story.details

            val circularProgress = CircularProgressDrawable(itemView.context)
            circularProgress.strokeWidth = 5f
            circularProgress.centerRadius = 30f
            circularProgress.start()
            Glide.with(itemView).load(story.url).placeholder(circularProgress).into(binding.storyImage)

            binding.storyImage.setOnClickListener {
                val bundle=Bundle().apply {
                    putParcelable("Story",story)
                }
                it.findNavController().navigate(R.id.storyDetailsFragment,bundle)
            }
        }
    }


    companion object StoryDiffUtil: DiffUtil.ItemCallback<Story>() {
        override fun areItemsTheSame(oldItem: Story, newItem: Story)=oldItem.storyId==newItem.storyId
        override fun areContentsTheSame(oldItem: Story, newItem: Story)=oldItem==newItem

    }


}