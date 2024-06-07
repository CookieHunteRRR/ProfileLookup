package com.cookiehunterrr.profilelookup.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cookiehunterrr.profilelookup.MainActivity
import com.cookiehunterrr.profilelookup.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment()
{
    private var _binding: FragmentHistoryBinding? = null
    private lateinit var historyAdapter : HistoryAdapter
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val historyViewModel =
            ViewModelProvider(this).get(HistoryViewModel::class.java)

        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        historyAdapter = HistoryAdapter(this.requireContext(), (this.activity as MainActivity).DB)
        binding.recyclerProfiles.adapter = historyAdapter
        binding.recyclerProfiles.layoutManager = LinearLayoutManager(this.requireContext())

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}