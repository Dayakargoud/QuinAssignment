package com.dayakar.quinassignment.ui.addStory

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.dayakar.quinassignment.AddStoryViewModel
import com.dayakar.quinassignment.UploadStatus
import com.dayakar.quinassignment.databinding.AddStoryFragmentBinding
import com.dayakar.quinassignment.model.Story
import com.google.firebase.auth.FirebaseAuth
import java.util.*


class AddStoryFragment : Fragment(),LocationListener {

    companion object {
        private const val REQUEST_CODE_IMAGE_PICK = 214
        private const val LOCATION_PERMISSION_CODE=198
    }

    private lateinit var viewModel: AddStoryViewModel
    private lateinit var binding:AddStoryFragmentBinding
    private var fileUri: Uri? = null

    private lateinit var mAuth:FirebaseAuth
    private var locationManager:LocationManager?=null
    private var currentLocation:String?=null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= AddStoryFragmentBinding.inflate(inflater)
        viewModel = activity?.let { ViewModelProvider(it).get(AddStoryViewModel::class.java) }!!
        mAuth= FirebaseAuth.getInstance()
        return binding.root

    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

             getLocation()

        binding.openGalleryButton.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type = "image/*"
                startActivityForResult(
                    it,
                    REQUEST_CODE_IMAGE_PICK
                )
            }
        }

        binding.postStoryButton.setOnClickListener {
            hideKeyBoard()
             binding.progressBar.visibility=View.VISIBLE
            val storyDesc=binding.storyTags.editText?.text?.trim().toString()
            val user=mAuth.currentUser
                 if (fileUri!=null && storyDesc.isNotEmpty()){
                     currentLocation?.let {
                         val story=Story(
                             storyId = System.currentTimeMillis(),
                             userName = user?.displayName.toString(),
                             userEmail = user?.email.toString(),
                             taggedLocation = it,
                             details = storyDesc
                         )
                         binding.postStoryButton.isEnabled=false
                         viewModel.uploadStory(story, fileUri!!)
                     }

                 }else{
                     binding.progressBar.visibility=View.GONE
                     if (fileUri==null){
                         Toast.makeText(context, "Please select a image", Toast.LENGTH_SHORT).show()
                     }
                     if (storyDesc.isEmpty()){
                         binding.storyTags.error="This field required"

                     }
                 }
        }
        viewModel.uploadStatus.observe(viewLifecycleOwner, Observer {
            if (it == UploadStatus.SUCCESS || it == UploadStatus.ERROR) {
                binding.progressBar.visibility = View.GONE
                binding.postStoryButton.isEnabled = true
                if (it== UploadStatus.SUCCESS){
                    findNavController().navigateUp()
                }
            }
        })

    }

    @SuppressLint("MissingPermission")
    private fun getLocation(){
        if (checkPermissions()){

            try {
                locationManager =  context?.applicationContext?.getSystemService(LOCATION_SERVICE) as LocationManager
                locationManager?.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    5000,
                    5F,
                    this
                )

            }catch (e: Exception){
                e.printStackTrace()
            }
        }else{
            requestPermissions()
        }

    }
    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            LOCATION_PERMISSION_CODE
        )
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            }
        }
    }


    private fun hideKeyBoard() {
        // Hide the keyboard.
        val inputMethodManager =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_IMAGE_PICK) {
            data?.data?.let {
                fileUri = it
                binding.storyImage.setImageURI(it)
                binding.postStoryButton.visibility=View.VISIBLE
                binding.openGalleryButton.visibility=View.INVISIBLE
            }
        }
    }

    override fun onLocationChanged(location: Location) {

        try {
            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            val addresses: List<Address> =
                geocoder.getFromLocation(location.latitude, location.longitude, 1)
            val city: String = addresses[0].locality
            val country=addresses[0].countryName
            currentLocation="$city , $country"
            binding.storyTaggedLocation.visibility=View.VISIBLE
            binding.storyTaggedLocation.text = currentLocation

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

}