package com.dayakar.quinassignment

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dayakar.quinassignment.model.Story
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


enum class UploadStatus{
    NONE,
     ERROR,
    SUCCESS
}

class AddStoryViewModel(application: Application) : AndroidViewModel(application) {
    private val context: Context =application.applicationContext
    private val storageRef = Firebase.storage.reference

    private var _uploadStatus=MutableLiveData<UploadStatus>()
     val uploadStatus:LiveData<UploadStatus> get() = _uploadStatus


    fun uploadStory(story: Story, fileUri: Uri){
        _uploadStatus.value=UploadStatus.NONE

        viewModelScope.launch {
            uploadImageToStorage(story, fileUri)
        }

    }

    private suspend fun uploadImageToStorage(story: Story, fileUri: Uri) = CoroutineScope(
        Dispatchers.IO).launch {

        try {
            fileUri.let { it ->
                val storyName = story.userName + "_${System.currentTimeMillis()}"
                val path = storageRef.child("Stories/Users/${story.userName}/$storyName")
                val task = path.putFile(it).await().task.isSuccessful
                if (task) {
                    val downloadURL = path.downloadUrl.await().toString()

                    withContext(Dispatchers.Main) {
                        val finalStory= Story(
                            story.storyId,
                            story.userName,
                            story.userEmail,
                            story.taggedLocation,
                            downloadURL,
                            story.details,
                            System.currentTimeMillis()

                        )

                        Log.d("Home Viewmodel", "uploadImageToStorage: uploading...")
                        val mAllDatabaseReference = FirebaseDatabase.getInstance().reference
                            .child("Stories/AllUsers").child(story.userName)

                        mAllDatabaseReference.child(story.storyId.toString()).setValue(finalStory)
                            .addOnSuccessListener {
                                //Story upload success
                                _uploadStatus.value=UploadStatus.SUCCESS

                                Toast.makeText(
                                    context,
                                    "Story uploaded",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }.addOnFailureListener {

                                //Story upload failed
                                Toast.makeText(
                                    context,
                                    "Upload failed ${it.message}",
                                    Toast.LENGTH_SHORT
                                ).show()

                                _uploadStatus.value=UploadStatus.ERROR
                            }


                    }


                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                _uploadStatus.value=UploadStatus.ERROR

                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()

            }
        }
    }

}