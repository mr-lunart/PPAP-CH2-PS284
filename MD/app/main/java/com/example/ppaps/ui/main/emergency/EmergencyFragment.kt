package com.example.ppaps.ui.main.emergency

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.GestureDetector
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.ppaps.R
import com.example.ppaps.databinding.FragmentEmergencyBinding
import com.example.ppaps.ui.main.home.HomeFragment

class EmergencyFragment : Fragment() {

    private var _binding: FragmentEmergencyBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: EmergencyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmergencyBinding.inflate(layoutInflater, container, false)
        val gradientDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.gradient_bg)
        view?.background = gradientDrawable
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = binding.myToolbar
        toolbar.title = "Panggilan Darurat"
        toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_arrow_back_24)

        val dataId = arguments?.getString(HomeFragment.EXTRA_ID)

        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.cvDarurat.setOnLongClickListener {
            if (binding.nohpEditText.text.toString().isNotEmpty()) {
                val mBundle = Bundle()
                mBundle.putString(HomeFragment.EXTRA_ID, dataId)
                it.findNavController().navigate(R.id.action_emergencyFragment_to_cameraFragment, mBundle)
                true
            } else {
                showToast("No HP tidak boleh kosong")
                false
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}