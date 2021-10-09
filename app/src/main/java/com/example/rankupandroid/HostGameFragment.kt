package com.example.rankupandroid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.rankupandroid.databinding.FragmentHostGameBinding
import com.example.rankupandroid.playerslist.PlayersListViewModel
import com.example.rankupandroid.playerslist.ToPlayersListFrom

class HostGameFragment : Fragment() {

    private lateinit var binding: FragmentHostGameBinding
    private lateinit var navCtrl: NavController
    private val sharedModel: SharedViewModelSelectedPlayers by activityViewModels()

    // use `activityViewModels` to retrieve the same instance bound to the lifeCycle. see
    // PlayersListFragment for details
    // Also note that no factory for PlayersListViewModel is provided here because it will be
    // first created by PlayersListFragment
    private val playersListModel: PlayersListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHostGameBinding.inflate(layoutInflater, container, false)
        navCtrl = findNavController()

        binding.apply {
            hostStartGameButton.setOnClickListener {
                if (sharedModel.myself.value == null || sharedModel.teammate.value == null ||
                    sharedModel.opponent1.value == null || sharedModel.opponent2.value == null
                ) {
                    Toast.makeText(
                        context, "Fail to start a game: all participants must be " +
                                "specified!!", Toast.LENGTH_LONG
                    ).show()
                } else {
                    navCtrl.navigate(R.id.action_hostGameFragment_to_rankUpFragment)
                }
            }

            sharedModel.myself.observe(viewLifecycleOwner, {
                playerMyself.updateView(it, false)
            })

            sharedModel.teammate.observe(viewLifecycleOwner, {
                playerTeammate.apply {
                    updateView(it, true)
                    setButtonAction(
                        genButtonAction(
                            sharedModel.teammate
                        )
                    )
                }
            })

            sharedModel.opponent1.observe(viewLifecycleOwner, {
                playerOpponent1.apply {
                    updateView(it, true)
                    setButtonAction(
                        genButtonAction(
                            sharedModel.opponent1
                        )
                    )
                }

            })

            sharedModel.opponent2.observe(viewLifecycleOwner, {
                playerOpponent2.apply {
                    updateView(it, true)
                    setButtonAction(
                        genButtonAction(
                            sharedModel.opponent2
                        )
                    )
                }
            })
        }


        return binding.root
    }

    // a higher order function where f binds the selected player to correct entry in the shared viewmodel
    // 1. Thought about passing in just the entry (e.g. sharedViewModel.yourTeammate), but parameters are passed as val so assignment doesn't work
    // 2. setting `var teammate = sharedViewModel.yourTeammate` then assign teammate to new values does NOT change sharedViewModel.yourTeammate
    private fun genButtonAction(
        playerLiveData: MutableLiveData<Player?>
    ): (View) -> Unit {
        if (playerLiveData.value == null) {
            // perform add functionality
            return { _: View ->
                // first set up shared view model
                sharedModel.callback = { selected: Player ->
                    playerLiveData.value = selected
                    navCtrl.navigateUp()
                }

                // then navigate
                // the following uses safe args to indicate to PlayersList from where it's navigated to
                val action = HostGameFragmentDirections.actionHostGameFragmentToPlayersListFragment(
                    ToPlayersListFrom.HostGameFrag
                )
                navCtrl.navigate(action)
            }

        } else {
            // perform remove functionality
            // after an item is selected, the + button will become a x button. The following
            // changes its click listener
            return { _: View ->
                // toggle this player before deletion
                val oldVal = playerLiveData.value!!
                playersListModel.toggleParticipation(oldVal)
                playerLiveData.value = null
            }
        }
    }

}