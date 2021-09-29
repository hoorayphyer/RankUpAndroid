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
            val actionToList = { _: View ->
                // the following uses safe args to indicate to PlayersList from where it's navigated to
                val action = HostGameFragmentDirections.actionHostGameFragmentToPlayersListFragment(
                    ToPlayersListFrom.HostGameFrag
                )
                navCtrl.navigate(action)
            }

            playerYourTeammate.setAddButtonAction {
                sharedModelPlayersList.apply {
                    callback = { selected: Player ->
                        yourTeammate = selected
                        navCtrl.navigateUp()
                    }
                }
                actionToList(it)
            }

            playerOpponent1.setAddButtonAction {
                sharedModelPlayersList.apply {
                    callback = { selected: Player ->
                        opponent1 = selected
                        navCtrl.navigateUp()
                    }
                }
                actionToList(it)
            }

            playerOpponent2.setAddButtonAction {
                sharedModelPlayersList.apply {
                    callback = { selected: Player ->
                        opponent2 = selected
                        navCtrl.navigateUp()
                    }
                }
                actionToList(it)
            }
        }

        // TODO consider using data binding for each player view, which may require implementing the Player class first
        return binding.root
    }
}