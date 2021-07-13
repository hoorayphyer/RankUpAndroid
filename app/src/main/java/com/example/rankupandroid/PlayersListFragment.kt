package com.example.rankupandroid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rankupandroid.databinding.FragmentPlayersListBinding

class PlayersListFragment : Fragment() {
    private lateinit var binding : FragmentPlayersListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayersListBinding.inflate(layoutInflater, container, false)

        val playersAdapter = PlayersListAdapter(
            PlayerItemClickListener {
                // TODO fix this when nav graph is set up
//                val navAction = MainFragmentDirections.actionShowDetail(it) // this is using safe-args, see https://developer.android.com/guide/navigation/navigation-pass-data
//                this.findNavController().navigate(navAction)
            }
        )

        binding.playerRecyclerView.adapter = playersAdapter
        return binding.root
    }
}