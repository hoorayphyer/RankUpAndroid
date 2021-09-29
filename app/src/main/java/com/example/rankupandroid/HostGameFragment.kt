package com.example.rankupandroid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.rankupandroid.databinding.FragmentHostGameBinding
import com.example.rankupandroid.playerslist.ToPlayersListFrom

class HostGameFragment : Fragment() {

    private lateinit var binding: FragmentHostGameBinding
    private lateinit var navCtrl: NavController
    private val sharedModelPlayersList: SharedViewModelHostGameFragPlayersListFrag by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHostGameBinding.inflate(layoutInflater, container, false)
        navCtrl = findNavController()

        binding.apply {
            hostStartGameButton.setOnClickListener {
                navCtrl.navigate(R.id.action_hostGameFragment_to_rankUpFragment)
            }

            // automatically add you as an existing player
            playerYou.setUpPlayerView("Me", R.drawable.my_avatar, true)

            // initialize other players
            playerYourTeammate.setAddButtonAction(genButtonAction {
                sharedModelPlayersList.yourTeammate = it
            })

            playerOpponent1.setAddButtonAction(genButtonAction {
                sharedModelPlayersList.opponent1 = it
            })

            playerOpponent2.setAddButtonAction(genButtonAction {
                sharedModelPlayersList.opponent2 = it
            })
        }

        // TODO consider using data binding for each player view, which may require implementing the Player class first
        return binding.root
    }

    // a higher order function where f binds the selected player to correct entry in the shared viewmodel
    // 1. Thought about passing in just the entry (e.g. sharedViewModel.yourTeammate), but parameters are passed as val so assignment doesn't work
    // 2. setting `var teammate = sharedViewModel.yourTeammate` then assign teammate to new values does NOT change sharedViewModel.yourTeammate
    private fun genButtonAction(f: (Player) -> Unit): (View) -> Unit {
        return { _: View ->
            // first set up shared view model
            sharedModelPlayersList.callback = { selected: Player ->
                f(selected)
                navCtrl.navigateUp()
            }

            // then navigate
            // the following uses safe args to indicate to PlayersList from where it's navigated to
            val action = HostGameFragmentDirections.actionHostGameFragmentToPlayersListFragment(
                ToPlayersListFrom.HostGameFrag
            )
            navCtrl.navigate(action)
        }
    }

}