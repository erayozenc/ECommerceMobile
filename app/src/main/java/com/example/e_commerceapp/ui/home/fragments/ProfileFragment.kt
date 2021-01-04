package com.example.e_commerceapp.ui.home.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.example.e_commerceapp.R
import com.example.e_commerceapp.adapter.FavoriteAdapter
import com.example.e_commerceapp.databinding.FragmentFavoritesBinding
import com.example.e_commerceapp.databinding.FragmentProfileBinding
import com.example.e_commerceapp.ui.auth.AuthViewModel
import com.example.e_commerceapp.ui.auth.MainActivity
import com.example.e_commerceapp.ui.home.viewmodel.ProductViewModel
import com.example.e_commerceapp.util.UserPreferences
import com.example.e_commerceapp.util.startNewActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()

    @Inject
    lateinit var preferences : UserPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonLogout.setOnClickListener {
            viewModel.logout()
            viewModel.deleteAuthToken()
        }

        preferences.authToken.asLiveData().observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()){
                requireActivity().startNewActivity(MainActivity::class.java)
            }
        })
    }

}