package com.example.rankupandroid.playerslist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.rankupandroid.SharedViewModelSelectedPlayers
import com.example.rankupandroid.databinding.FragmentPlayersListBinding

enum class ToPlayersListFrom {HostGameFrag}

class PlayersListFragment : Fragment() {
    private lateinit var binding : FragmentPlayersListBinding
    private val viewModel = PlayersListViewModel(PlayersDataRepository())
    private val args : PlayersListFragmentArgs by navArgs()

    private val sharedModelHostGame : SharedViewModelSelectedPlayers by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayersListBinding.inflate(layoutInflater, container, false)

        val callback = when (args.from) {
            ToPlayersListFrom.HostGameFrag -> sharedModelHostGame.callback
        }

        val playersAdapter = PlayersListAdapter(PlayerItemClickListener(callback))

        // the following supplies the recycler view with concrete contents
        viewModel.activePlayers.observe(viewLifecycleOwner, {
            it?.let{
                playersAdapter.submitList(it)
            }
        })

        binding.playerRecyclerView.adapter = playersAdapter
        return binding.root
    }
}