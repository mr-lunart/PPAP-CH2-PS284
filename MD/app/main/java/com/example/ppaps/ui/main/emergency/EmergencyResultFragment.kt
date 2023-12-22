package com.example.ppaps.ui.main.emergency

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.ppaps.R
import com.example.ppaps.databinding.FragmentEmergencyBinding
import com.example.ppaps.databinding.FragmentEmergencyResultBinding
import com.example.ppaps.ui.main.home.HomeFragment

class EmergencyResultFragment : Fragment() {
    private var _binding: FragmentEmergencyResultBinding? = null
    private val binding get() = _binding!!
    private val handler = Handler(Looper.getMainLooper())
    private val delay: Long = 3000
    private lateinit var runnable: Runnable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmergencyResultBinding.inflate(layoutInflater, container, false)
        val gradientDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.gradient_bg)
        _binding?.root?.background = gradientDrawable
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = binding.myToolbar
        toolbar.title = "Panggilan Darurat"
        toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        when (arguments?.getString("result")) {
            "true" -> {
                binding.resultIV.setImageResource(R.drawable.baseline_check_circle_outline_24)
                binding.resultTV.text = getString(R.string.verification_success)

                runnable = object : Runnable {
                    override fun run() {
                        changeFragment()
                        handler.postDelayed(this, delay)
                    }
                }

                handler.postDelayed(runnable, delay)
            }
            "false" -> {
                binding.resultIV.setImageResource(R.drawable.cancel_24px)
                binding.resultTV.text = getString(R.string.verification_failed)

                runnable = object : Runnable {
                    override fun run() {
                        backtoCamera()
                        handler.postDelayed(this, delay)
                    }
                }

                handler.postDelayed(runnable, delay)
            }
        }
    }


    private fun changeFragment() {
        findNavController().navigate(R.id.action_emergencyResultFragment_to_callFragment)
    }

    private fun backtoCamera() {
        findNavController().navigate(R.id.action_emergencyResultFragment_to_cameraFragment3)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(runnable)
    }
}