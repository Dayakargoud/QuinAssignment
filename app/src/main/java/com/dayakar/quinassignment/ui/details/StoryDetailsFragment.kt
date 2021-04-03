package com.dayakar.quinassignment.ui.details

import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.dayakar.quinassignment.databinding.StoryDetailsFragmentBinding
import com.dayakar.quinassignment.model.Story

class StoryDetailsFragment : Fragment() {



    private lateinit var binding:StoryDetailsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= StoryDetailsFragmentBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val story:Story?=arguments?.getParcelable("Story")
        Glide.with(requireContext()).load(story?.url).into(binding.storyImage)
        binding.postUserName.text=story?.userName
        binding.postTaggedLocation.text=story?.taggedLocation
        binding.storyDetails.text=story?.details
        binding.uploadTime.text=getUploadTime(story?.postedTime)


    }

    private fun getUploadTime(uploadTime:Long?):String{
        val currentTime=System.currentTimeMillis()
        uploadTime?.let {
            val ago= DateUtils.getRelativeTimeSpanString(uploadTime,currentTime,DateUtils.MINUTE_IN_MILLIS)
            return ago.toString()
        }

        return "While ago"
    }


}