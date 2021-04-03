package com.dayakar.quinassignment.model


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/*
* Created By DAYAKAR GOUD BANDARI on 03-04-2021.
*/


@Parcelize
data class Story(val storyId:Long=0L,val userName:String="",val userEmail:String="",val taggedLocation:String="",
                      val url:String="",val details:String="", val postedTime:Long=0L) : Parcelable