package com.dayakar.quinassignment.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dayakar.quinassignment.model.Story
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class HomeViewModel : ViewModel() {

    private val currentSearch=MutableLiveData<String>()



    private val storageRef = Firebase.storage.reference
    private var _allStories = MutableLiveData<List<Story>>()
    val allStories: LiveData<List<Story>> get() = _allStories

     var searchList=MutableLiveData<List<Story>>()

    init {
             loadAllStories()
    }

    fun searchStories(query:String){

              val list=allStories.value

           list?.let {
               searchList.value=filterTweets(query,it)
           }
    }


    private fun filterTweets(query: String,tweets:List<Story>):List<Story>{
        return tweets.filter { it.userName.contains(query,true)}

    }

    private fun  loadAllStories(){
        val path="Stories/AllUsers"
        loadAllStoriesFromFirebase(path)
    }
    private fun loadAllStoriesFromFirebase(refPath:String) {
        val mDatabase =
            FirebaseDatabase.getInstance().reference.child(refPath)
        mDatabase.keepSynced(true)

        mDatabase.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {


                val listEvent = mutableListOf<Story>()
                for (postSnapshot in dataSnapshot.children) {
                    for (eventSnapShot in postSnapshot.children) {
                        val item: Story? = eventSnapShot.getValue(Story::class.java)
                        if (item != null) {

                            listEvent.add(item)
                            println(item)
                        }

                    }

                }
                val sortedList = listEvent.sortedBy { it.postedTime }.reversed() as MutableList
                _allStories.value = sortedList

            }

            override fun onCancelled(databaseError: DatabaseError) {


            }
        })

    }



}