package com.example.ppaps.ui.main.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.ppaps.R
import com.example.ppaps.data.ResultState
import com.example.ppaps.databinding.FragmentHomeBinding
import com.example.ppaps.ui.ViewModelFactory
import com.example.ppaps.ui.signin.SigninViewModel
import com.example.ppaps.ui.verification.VerificationActivity
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var userid = ""
    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val gradientDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.gradient_bg)
        view?.background = gradientDrawable

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getToken()

        binding.cvDarurat.setOnClickListener {
            lifecycleScope.launch {
                viewModel.checkVerif(userid).observe(requireActivity()) {
                    when (it) {
                        is ResultState.Success -> {
                            val mBundle = Bundle()
                            mBundle.putString(EXTRA_ID, userid)
                            findNavController().navigate(R.id.action_navigation_home_to_emergencyFragment, mBundle)
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

        binding.cvPesan.setOnClickListener {
            it.findNavController().navigate(R.id.action_navigation_home_to_bookingFragment)
        }
    }

    private fun getToken() {
        viewModel.getSession().observe(requireActivity()) {
            lifecycleScope.launch {
                viewModel.getUser(it.token).observe(requireActivity()) {
                    when (it) {
                        is ResultState.Success -> {
                            showLoading(false)
                            val username = it.data.user?.username!!
                            userid = it.data.user.idUser!!
                            showUsername(username)
                        }
                        is ResultState.Loading -> { showLoading(true) }
                        is ResultState.Error -> {
                            showLoading(false)
                            AlertDialog.Builder(requireContext()).apply {
                                setTitle("Error")
                                setMessage("Akun belum/dalam proses verifikasi")
                                setPositiveButton("Lanjut") { _, _ -> }
                                create()
                                show()
                            }
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun showUsername(username: String?) {
        binding.userGreeting.text = "Selamat datang, ${username.orEmpty()}"
    }
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val EXTRA_ID = "userid"
    }
}