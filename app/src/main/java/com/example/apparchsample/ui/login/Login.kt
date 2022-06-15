package com.example.apparchsample.ui.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.apparchsample.R
import com.example.apparchsample.databinding.FragmentLoginBinding
import com.example.apparchsample.util.LoadingDialog


class Login : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var sharedPreferences: SharedPreferences

    private val viewModel: LoginViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(
            this,
            LoginViewModel.Factory(activity.application)
        )[LoginViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        sharedPreferences = context?.getSharedPreferences("Sunrise", Context.MODE_PRIVATE)!!
        loadingDialog = LoadingDialog(requireContext())
        btnOnClick()
        observeLoginResponse()
        observeNetworkError()
        return binding.root
    }

    private fun btnOnClick() {
        with(viewModel) {
            binding.btnLogin.setOnClickListener {
                if (this.email.get() != null && this.password.get() != null) {
                    loadingDialog.show()
                    this.setLogin(email.get()!!.trim(), password.get()!!.trim())
                } else {
                    Toast.makeText(context, "Please fill both field", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun observeLoginResponse() {
        viewModel.loginResponse.observe(viewLifecycleOwner) {
            loadingDialog.dismiss()
            if (it != null) {
                storeToken(it.user.token)
                requireView().findNavController().navigate(R.id.dashboard)
                Log.d("#Stam", it.toString())
            }
        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun storeToken(token: String) {
        sharedPreferences.edit().putString("token", token).apply()
    }

    private fun observeNetworkError() {
        with(viewModel) {
            isNetworkError.observe(viewLifecycleOwner) {
                if (it) {
                    loadingDialog.dismiss()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
    }

}