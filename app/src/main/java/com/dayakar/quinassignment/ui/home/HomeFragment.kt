package com.dayakar.quinassignment.ui.home

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.dayakar.quinassignment.R
import com.dayakar.quinassignment.databinding.HomeFragmentBinding
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {



    private lateinit var viewModel: HomeViewModel
    private lateinit var binding:HomeFragmentBinding
    private lateinit var mAuth:FirebaseAuth


   private lateinit var  storyAdapter: StoriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= HomeFragmentBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        mAuth= FirebaseAuth.getInstance()
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



         storyAdapter= StoriesAdapter()
        binding.storyRecyclerView.apply {

            adapter=storyAdapter
            setHasFixedSize(true)
        }



        viewModel.allStories.observe(viewLifecycleOwner, Observer {
               if (it!=null){
                   storyAdapter.submitList(it)
                   binding.progressBar.visibility=View.GONE
               }
        })



    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu,menu)

        val searchItem=menu.findItem(R.id.action_search)

        val searchView =searchItem.actionView as androidx.appcompat.widget.SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    viewModel.searchStories(query)
                    storyAdapter.submitList(viewModel.searchList.value)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    viewModel.searchStories(newText)
                    storyAdapter.submitList(viewModel.searchList.value)
                }
                return true
            }


        })

        searchView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View?) {

            }

            override fun onViewDetachedFromWindow(v: View?) {
                storyAdapter.submitList(viewModel.searchList.value)
            }

        })


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {

            R.id.logout -> {

                val currentUser = mAuth.currentUser
                if (currentUser != null) {

                    FirebaseAuth.getInstance().signOut()
                   findNavController().navigate(R.id.loginFragment)
                }
            }
            R.id.add_story->{
                findNavController().navigate(R.id.addStoryFragment)
            }

        }

        return super.onOptionsItemSelected(item)
    }



    override fun onStart() {
        super.onStart()
        val user=mAuth.currentUser
        if (user==null){
            findNavController().navigate(R.id.loginFragment)
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