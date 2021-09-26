package com.example.rankupandroid.playerslist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rankupandroid.databinding.FragmentPlayersListBinding

class PlayersListFragment : Fragment() {
    private lateinit var binding : FragmentPlayersListBinding

    private val viewModel = PlayersListViewModel(PlayersDataRepository())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Logic of click listener setup
        // 1. in list_item_asteroid.xml, there is a variable called clickListener whose onClick call is bound to the view's onClick attribute
        // 2. it is viewHolder's responsibility to bind that variable with an instance. It might be tempting to just instantiate such an instance inside the viewholder's calss, but it's not recommeneded because view holder should not maintain the logic of onClick behavior.
        // 3. for the click listener, we want navigation (which needs the current fragment to retrieve the navController), and customizing the fields with actual data (which is held in view model)
        // 4. in viewHolder, when clickListener is bound, the corresponding asteroid object is available.
        // 5. we use safe-args to pass values during navigation. This value can be set in clickListener

        binding = FragmentPlayersListBinding.inflate(layoutInflater, container, false)

        val playersAdapter = PlayersListAdapter(
            PlayerItemClickListener {
                // TODO fix this when nav graph is set up
//                val navAction = MainFragmentDirections.actionShowDetail(it) // this is using safe-args, see https://developer.android.com/guide/navigation/navigation-pass-data
//                this.findNavController().navigate(navAction)
            }
        )

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