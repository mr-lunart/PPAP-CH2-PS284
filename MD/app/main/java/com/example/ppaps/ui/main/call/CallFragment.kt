package com.example.ppaps.ui.main.call

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.ppaps.R
import com.example.ppaps.databinding.FragmentCallBinding
import com.example.ppaps.databinding.FragmentHistoryBinding
import com.example.ppaps.ui.ViewModelFactory
import kotlinx.coroutines.launch

class CallFragment : Fragment() {
    private var _binding: FragmentCallBinding? = null
    private val binding get() = _binding!!
    private val handler = Handler(Looper.getMainLooper())
    private val delay: Long = 3000
    private lateinit var runnable: Runnable
    private val viewModel by viewModels<CallViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCallBinding.inflate(inflater, container, false)
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

    private fun getHospitals() {
        viewModel.getSession().observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                viewModel.getHospital(it.token).observe(viewLifecycleOwner) {

                }
            }
        }
    }

    private fun changeFragment() {
        Log.d("CallFragment", "Memanggil findnavcontroller")
        findNavController().navigate(R.id.action_callFragment_to_emergencyConfirmFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(runnable)
    }
}