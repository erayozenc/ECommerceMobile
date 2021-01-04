package com.example.e_commerceapp.ui.home.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.e_commerceapp.R
import com.example.e_commerceapp.adapter.ProductAdapter
import com.example.e_commerceapp.databinding.FragmentHomepageBinding
import com.example.e_commerceapp.responses.auth.User
import com.example.e_commerceapp.responses.products.Product
import com.example.e_commerceapp.ui.home.dialogs.SortBottomSheetDialog
import com.example.e_commerceapp.ui.home.viewmodel.ProductViewModel
import com.example.e_commerceapp.util.Constants.SEARCH_TIME_DELAY
import com.example.e_commerceapp.util.Resource
import com.example.e_commerceapp.util.handleApiError
import com.example.e_commerceapp.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomepageFragment : Fragment(R.layout.fragment_homepage), SortBottomSheetDialog.SortByListener {

    private var _binding : FragmentHomepageBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductViewModel by viewModels()
    private lateinit var productAdapter : ProductAdapter
    private var token : String? = null
    private val args : HomepageFragmentArgs by navArgs()
    private var category: String? = null
    private var favProductIdList = listOf<String>()
    private lateinit var user : User

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomepageBinding.inflate(inflater, container, false)
        lifecycleScope.launch {
            token = viewModel.getAuthToken()
            viewModel.getFavoriteProductIdList(token)
            viewModel.getCurrentUser(token)
        }

        category = args.category
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressbar.visible(true)
        observeCurrentUser()
        observeFavProductIds()
        //lifecycleScope.launch { viewModel.getProductList(viewModel.getAuthToken(),"asc","_id",10) }
        setupRecyclerView()
        //observeListProducts()

        //Just for searching product
        //if(binding.etSearch.text.toString().isNotEmpty())
        observeSearchListProducts()

        var job : Job? = null
        binding.etSearch.addTextChangedListener { editable ->
            if (editable.toString().isNotEmpty()){
                job?.cancel()
                job = MainScope().launch {
                    delay(SEARCH_TIME_DELAY)
                    editable?.let {
                        viewModel.getSearchProductList(token ,editable.toString(), null)
                    }
                }
            }
        }

        binding.buttonSortBy.setOnClickListener {
            val dialog = SortBottomSheetDialog()
            dialog.show(parentFragmentManager, "BottomSheetDialog")
            dialog.setTargetFragment(this, 1)
        }

        productAdapter.setOnItemClickListener { id, adding ->
            when(adding){
                true ->{
                    viewModel.addFavoriteProduct(token, id)
                }
                false ->{
                    viewModel.removeFavoriteProduct(token, id)
                }
            }
        }

        binding.buttonAddProduct.setOnClickListener {
            findNavController().navigate(R.id.action_homepageFragment_to_addProductFragment)
        }

    }


    private fun setupRecyclerView(){
        productAdapter = ProductAdapter()
        binding.recyclerView.apply {
            adapter = productAdapter
            layoutManager = GridLayoutManager(requireContext(),2)

        }
    }

    private fun observeFavProductIds(){
        viewModel.productFavoriteIdListResponse.observe(viewLifecycleOwner, Observer {
            binding.progressbar.visible(it is Resource.Loading)
            when(it){
                is Resource.Success ->{
                    viewModel.getSearchProductList(token,"", category)
                    favProductIdList = it.value
                    productAdapter.favIdList = favProductIdList
                }
                is Resource.Failure ->{
                    handleApiError(it)
                }
            }
        })
    }

    private fun observeListProducts(){
        viewModel.productListResponse.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Success-> {
                    if (it.value.products.isNotEmpty()){
                        productAdapter.differ.submitList(it.value.products)
                    }
                }
                is Resource.Failure ->{
                    handleApiError(it)
                }
            }
        })
    }

    private fun observeSearchListProducts(){
        viewModel.productSearchListResponse.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Success-> {
                    productAdapter.differ.submitList(it.value.products)
                    println(it.value.products)
                }
                is Resource.Failure ->{
                    handleApiError(it)
                    println("error" + it)
                }
            }
        })
    }

    private fun observeCurrentUser(){
        viewModel.currentUserResponse.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Success ->{
                    user = it.value.user
                    binding.buttonAddProduct.visible(user.isAdmin)
                }
                is Resource.Failure ->{
                    handleApiError(it)
                }

            }
        })
    }

    override fun getSortedList(products: List<Product>) {
        productAdapter.differ.submitList(products)
        products.map { product -> println("Fragment -> " + product.price) }
    }
}