package com.example.myapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentSpaceBinding

class SpaceFragment : Fragment() {

    private var binding: FragmentSpaceBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSpaceBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        for ((index,e) in binding?.viewGroup?.children!!.withIndex()) {
            e.setOnClickListener {
                when (index) {

                }
            }
        }
    }
}