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

        val historiesAdapter = GameHistoryListAdapter()

        viewModel.gameHistories.observe(viewLifecycleOwner, {
            historiesAdapter.addHeaderAndSubmitList(it)
        })

        binding.historyRecyclerView.adapter = historiesAdapter

        binding.actionButtonGameHistory.setOnClickListener {
            viewModel.deleteHistories()
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadHistories()
    }
}