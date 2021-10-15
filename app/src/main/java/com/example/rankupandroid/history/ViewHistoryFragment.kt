package com.example.rankupandroid.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.rankupandroid.databinding.FragmentViewHistoryBinding

class ViewHistoryFragment : Fragment() {
    private lateinit var binding: FragmentViewHistoryBinding

    private val viewModel: ViewHistoryViewModel by activityViewModels {
        val dao = GameHistoryDatabase.getInstance(requireActivity().application).dao
        val repo = GameHistoryDataRepository(dao)
        ViewHistoryViewModelFactory(repo)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentViewHistoryBinding.inflate(layoutInflater, container, false)


        val itemClickListener = GameHistoryItemClickListener { }

        val historiesAdapter = GameHistoryListAdapter(itemClickListener)

        viewModel.repo.gameHistories.observe(viewLifecycleOwner, {
            historiesAdapter.submitList(it)
        })

        binding.historyRecyclerView.adapter = historiesAdapter
        return binding.root
    }
}