package com.dayakar.quinassignment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest


sealed class LoginStatus{
    class Failed(val msg:String?):LoginStatus()
    object Success : LoginStatus()
    object None:LoginStatus()

}
class LoginViewModel : ViewModel() {
    private val mAuth=FirebaseAuth.getInstance()
    private var _loginStatus=MutableLiveData<LoginStatus>()
     val loginStatus:LiveData<LoginStatus> get() = _loginStatus


    private var _signUpStatus=MutableLiveData<LoginStatus>()
    val signUpStatus:LiveData<LoginStatus> get() = _signUpStatus

    fun loginUser(email:String, password:String){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
            if (it.isSuccessful){
                _loginStatus.value=LoginStatus.Success
            }
        }.addOnFailureListener {
            _loginStatus.value=LoginStatus.Failed(it.message)
        }

    }

    fun removeSuccessCallback(){
        _loginStatus.value=LoginStatus.None
    }

     fun signUpUser(email:String,password:String,userName:String){
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            if (it.isSuccessful){

                val user=mAuth.currentUser
                val updateProfile= UserProfileChangeRequest.Builder()
                    .setDisplayName(userName)
                    .build()

                user?.updateProfile(updateProfile)?.addOnSuccessListener {
                    _signUpStatus.value=LoginStatus.Success

                }?.addOnFailureListener { it1->
                    _signUpStatus.value=LoginStatus.Failed(it1.message)

                }

            }
        }.addOnFailureListener {
            _signUpStatus.value=LoginStatus.Failed(it.message)


        }
    }


}