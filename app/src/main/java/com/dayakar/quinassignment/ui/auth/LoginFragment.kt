package com.dayakar.quinassignment.ui.auth

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.dayakar.quinassignment.LoginStatus
import com.dayakar.quinassignment.LoginViewModel
import com.dayakar.quinassignment.R
import com.dayakar.quinassignment.databinding.LoginFragmentBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {


    //shared viewmodel between login and signup fragment
    private  val viewModel by activityViewModels<LoginViewModel>()
    private lateinit var binding:LoginFragmentBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= LoginFragmentBinding.inflate(inflater)
        mAuth= FirebaseAuth.getInstance()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener {
            val password=binding.loginPassword.editText?.text.toString()
            val email=  binding.loginEmail.editText?.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty()){
                binding.progressbar.visibility=View.VISIBLE
                viewModel.loginUser(email, password)
                binding.loginButton.isEnabled=false
            }else{
                if (email.isEmpty()){
                    Toast.makeText(context, "Email can't be empty", Toast.LENGTH_SHORT).show()

                }
                if (password.isEmpty()){
                    Toast.makeText(context, "Password can't be empty", Toast.LENGTH_SHORT).show()

                }
            }


        }
        binding.loginSignUpText.setOnClickListener {

            findNavController().navigate(R.id.singnUpFragment)
        }

        viewModel.loginStatus.observe(viewLifecycleOwner, Observer {

            when(it){

                is LoginStatus.Success->{
                    binding.progressbar.visibility=View.VISIBLE
                    Toast.makeText(context, "Login Success", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
                is LoginStatus.Failed->{
                    binding.progressbar.visibility=View.INVISIBLE
                    Toast.makeText(context,it.msg , Toast.LENGTH_SHORT).show()
                    binding.loginButton.isEnabled=true
                }
            }



        })

    }

    override fun onStart() {
        super.onStart()
        val user=mAuth.currentUser
        if (user!=null){
            findNavController().navigate(R.id.homeFragment)
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(
            true // default to enabled
        ) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,  // LifecycleOwner
            callback
        )
    }






}