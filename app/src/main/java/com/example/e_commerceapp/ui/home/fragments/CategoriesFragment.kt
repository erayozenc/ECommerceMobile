package com.example.e_commerceapp.ui.home.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.e_commerceapp.R
import com.example.e_commerceapp.adapter.CategoryAdapter
import com.example.e_commerceapp.adapter.ProductAdapter
import com.example.e_commerceapp.databinding.FragmentCategoriesBinding
import com.example.e_commerceapp.databinding.FragmentHomepageBinding
import com.example.e_commerceapp.ui.home.viewmodel.CategoryViewModel
import com.example.e_commerceapp.ui.home.viewmodel.ProductViewModel
import com.example.e_commerceapp.util.Resource
import com.example.e_commerceapp.util.handleApiError
import com.example.e_commerceapp.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoriesFragment : Fragment(R.layout.fragment_categories) {

    private var _binding : FragmentCategoriesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CategoryViewModel by viewModels()
    private lateinit var categoryAdapter : CategoryAdapter
    private var token : String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        lifecycleScope.launch {
            token = viewModel.getAuthToken()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressbar.visible(false)
        setupRecyclerView()

        viewModel.getCategoryList(token)

        observeCategoryList()

    }

    private fun setupRecyclerView(){
        categoryAdapter = CategoryAdapter()
        binding.recyclerView.apply {
            adapter = categoryAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun observeCategoryList(){
        viewModel.categoryListResponse.observe(viewLifecycleOwner, Observer {
            binding.progressbar.visible(it is Resource.Loading)
            when(it){
                is Resource.Success ->{
                    categoryAdapter.differ.submitList(it.value.categories.minus(it.value.categories[0]))
                }
                is Resource.Failure ->{
                    handleApiError(it)
                }
            }
        })
    }
}