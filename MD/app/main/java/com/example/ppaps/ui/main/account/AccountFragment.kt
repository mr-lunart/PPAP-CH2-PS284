package com.example.ppaps.ui.main.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.ppaps.R
import com.example.ppaps.data.ResultState
import com.example.ppaps.databinding.FragmentAccountBinding
import com.example.ppaps.ui.ViewModelFactory
import com.example.ppaps.ui.main.home.CameraViewModel
import kotlinx.coroutines.launch

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<AccountViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val gradientDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.gradient_bg)
        view?.background = gradientDrawable
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getUserData()
    }

    private fun getUserData() {
        viewModel.getSession().observe(requireActivity()) {
            lifecycleScope.launch {
                viewModel.getUser(it.token).observe(requireActivity()) {
                    when (it) {
                        is ResultState.Success -> {
                            binding.usernameTextView.text = it.data.user?.username
                            binding.phoneNumberTextView.text = it.data.user?.noHp
                            Glide.with(requireContext())
                                .load(R.drawable.photos)
                                .centerCrop()
                                .into(binding.ivUser)
                        }
                        is ResultState.Loading -> {  }
                        is ResultState.Error -> {
                            showToast(it.message!!)
                        }
                        else -> {}
                    }
                }
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