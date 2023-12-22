package com.example.ppaps.ui.main.confirm

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.postDelayed
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import com.example.ppaps.R
import com.example.ppaps.databinding.FragmentEmergencyConfirmBinding
import com.example.ppaps.ui.main.geolocation.MapsFragment
import kotlinx.coroutines.Runnable

class EmergencyConfirmFragment : Fragment() {
    private var _binding: FragmentEmergencyConfirmBinding? = null
    private val binding get() = _binding!!

    private val handler = Handler(Looper.getMainLooper())
    private val delay: Long = 3000
    private lateinit var runnable: Runnable
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentEmergencyConfirmBinding.inflate(inflater, container, false)
        val gradientDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.gradient_bg)
        view?.background = gradientDrawable
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        runnable = object : Runnable {
            override fun run() {
                changeFragment()
                handler.postDelayed(this, delay)
            }
        }

        handler.postDelayed(runnable, delay)
    }

    private fun changeFragment() {
        findNavController().navigate(R.id.action_emergencyConfirmFragment_to_trackingFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(runnable)
    }
}