package com.kuroi.guardplant.ui.myPlants

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kuroi.guardplant.databinding.FragmentMyPlantsBinding

class MyPlantsFragment : Fragment() {

    private var _binding: FragmentMyPlantsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val myPlantsViewModel =
            ViewModelProvider(this).get(MyPlantsViewModel::class.java)

        _binding = FragmentMyPlantsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textMyPlants
        myPlantsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}