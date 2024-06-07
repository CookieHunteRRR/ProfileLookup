package com.cookiehunterrr.profilelookup.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.cookiehunterrr.profilelookup.MainActivity
import com.cookiehunterrr.profilelookup.R
import com.cookiehunterrr.profilelookup.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.buttonCopy.setOnClickListener {
            homeViewModel.onCopyButtonClicked(this.requireContext(), binding.textProfileurl.text.toString())
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        updateCurrentUser()
    }

    fun updateCurrentUser() {
        val user = (activity as MainActivity).currentUser!!

        binding.textUserid.text = user.id
        binding.textUsername.text = user.name
        binding.textRealname.text = user.realName
        binding.textCountrycode.text = "(${user.locCountryCode})"
        binding.textProfileurl.text = user.profileUrl
        setNewAvatar(user.avatarLink)
    }

    fun setNewAvatar(link: String) {
        Glide.with(this)
            .load(link)
            .into(binding.imageAvatar)
    }
}