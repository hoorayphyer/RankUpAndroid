package com.example.rankupandroid.playerslist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.rankupandroid.SharedViewModelSelectedPlayers
import com.example.rankupandroid.databinding.FragmentPlayersListBinding

enum class ToPlayersListFrom { HostGameFrag }

class PlayersListFragment : Fragment() {
    private lateinit var binding: FragmentPlayersListBinding

    private val viewModel: PlayersListViewModel by viewModels(
        ownerProducer = { requireParentFragment() },
        factoryProducer = {
            PlayersListViewModelFactory(PlayersDataRepository())
        }
    )
    private val args: PlayersListFragmentArgs by navArgs()

    private val sharedModelHostGame: SharedViewModelSelectedPlayers by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayersListBinding.inflate(layoutInflater, container, false)

        val callback = when (args.from) {
            ToPlayersListFrom.HostGameFrag -> sharedModelHostGame.callback
        }

        val itemClickListener = PlayerItemClickListener {
            viewModel.updatePlayer(it, participated = true)
            callback(it)
        }

        val playersAdapter = PlayersListAdapter(itemClickListener)

        // the following supplies the recycler view with concrete contents
        viewModel.activePlayers.observe(viewLifecycleOwner, {
            playersAdapter.submitList(it)
        })

        binding.playerRecyclerView.adapter = playersAdapter
        return binding.root
    }
}