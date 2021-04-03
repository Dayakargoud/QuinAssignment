package com.dayakar.quinassignment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.dayakar.quinassignment.databinding.SignUpFragmentBinding

class SingnUpFragment : Fragment() {



    private  val viewModel by activityViewModels<LoginViewModel>()
    private lateinit var binding:SignUpFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= SignUpFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonSignup.setOnClickListener {
            val email=binding.emailSignUp.editText?.text?.trim().toString()
            val password=binding.passwordSignup.editText?.text?.trim().toString()
            val userName=binding.usernameSignup.editText?.text?.trim().toString()

            if(email.isNotEmpty() && password.isNotEmpty() && userName.isNotEmpty()){
                binding.progressbar.visibility=View.VISIBLE
                viewModel.signUpUser(email, password, userName)
                binding.buttonSignup.isEnabled=false
            }else{
                if (email.isEmpty()){
                    Toast.makeText(context, "Email can't be empty", Toast.LENGTH_SHORT).show()

                }
                if (password.isEmpty()){
                    Toast.makeText(context, "Password can't be empty", Toast.LENGTH_SHORT).show()

                }
                if (userName.isEmpty()){
                    Toast.makeText(context, "Username can't be empty", Toast.LENGTH_SHORT).show()

                }
            }
        }

        binding.loginSignUpText.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.signUpStatus.observe(viewLifecycleOwner, Observer {

            when(it){

                is LoginStatus.Success->{
                    binding.progressbar.visibility=View.GONE
                    Toast.makeText(context, "SignUp Success", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.homeFragment)
                }
                is LoginStatus.Failed->{
                    Toast.makeText(context, "SignUp Failed ${it.msg}", Toast.LENGTH_SHORT).show()
                    binding.progressbar.visibility=View.GONE
                    binding.buttonSignup.isEnabled=true
                }
            }


    })

    }


}