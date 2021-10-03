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

enum class ToPlayersListFrom { HostGameFrag }

class PlayersListFragment : Fragment() {
    private lateinit var binding: FragmentPlayersListBinding

    // use `activityViewModels` to retrieve the same instance as in HostGameFragment
    // didn't use `viewModels()` because navigation doesn't create a parent-child relationship between
    // two fragments

    /* Below is the comment from a mentor
    *
    * When we get the ViewModel using `by viewModels()`, the ViewModel will be created for the
    * caller's Lifecycle and will create if not created. Hence, in Fragment's, this will create a
    * new instance of the ViewModel as both Fragment's will have different Lifecycle. Instead, you
    *  will need to use `by activityViewModels()` which will create the ViewModel for Activity
    * Lifecycle and since multiple Fragment's are hoisted within the same Activity Lifecycle,
    * using this method will create the ViewModel only once and will return the same instance in
    * subsequent calls.
    * */


    private val viewModel: PlayersListViewModel by activityViewModels {
        PlayersListViewModelFactory(PlayersDataRepository())
    }
    private val args: PlayersListFragmentArgs by navArgs()

    private val sharedModel: SharedViewModelSelectedPlayers by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayersListBinding.inflate(layoutInflater, container, false)

        val callback = when (args.from) {
            ToPlayersListFrom.HostGameFrag -> sharedModel.callback
        }

        val itemClickListener = PlayerItemClickListener {
            viewModel.toggleParticipation(it)
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