package com.example.e_commerceapp.ui.home.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_commerceapp.R
import com.example.e_commerceapp.adapter.FavoriteAdapter
import com.example.e_commerceapp.adapter.ProductAdapter
import com.example.e_commerceapp.databinding.FragmentFavoritesBinding
import com.example.e_commerceapp.databinding.FragmentHomepageBinding
import com.example.e_commerceapp.responses.cart.Cart
import com.example.e_commerceapp.ui.home.viewmodel.ProductViewModel
import com.example.e_commerceapp.util.Resource
import com.example.e_commerceapp.util.handleApiError
import com.example.e_commerceapp.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_favorites.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private var _binding : FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductViewModel by viewModels()
    private lateinit var favoriteAdapter : FavoriteAdapter
    private var token : String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        lifecycleScope.launch {
            token = viewModel.getAuthToken()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressbar.visible(true)
        setupRecyclerView()
        viewModel.getFavoriteProductList(token)

        favoriteAdapter.setOnItemClickListener {product, selection ->
            when(selection){
                //For adding favorite product to basket.
                1->{
                    val cart = Cart(0, product)
                    viewModel.addProductToCart(cart).apply {
                        Toast.makeText(requireContext(), "Ürün başarıyla sepete eklendi.", Toast.LENGTH_SHORT).show()
                    }
                }
                //For removing the favorite product
                2->{
                    viewModel.removeFavoriteProduct(token, product._id)
                }
                //For navigating the selected favorite product
                3->{
                    val bundle = Bundle().apply {
                        putSerializable("product",product)
                    }
                    findNavController().navigate(R.id.action_favoritesFragment_to_productDetailFragment,bundle)
                }
            }
        }

        binding.buttonNoFav.setOnClickListener {
            findNavController().navigate(R.id.action_favoritesFragment_to_homepageFragment)
        }

        observeFavListProducts()
        observeRemovedFavList()
    }

    private fun setupRecyclerView(){
        favoriteAdapter = FavoriteAdapter()
        binding.recyclerView.apply {
            adapter = favoriteAdapter
            layoutManager = LinearLayoutManager(requireContext())

        }
    }

    private fun observeFavListProducts(){
        viewModel.productFavoriteListResponse.observe(viewLifecycleOwner, Observer {
            binding.progressbar.visible(it is Resource.Loading)
            when(it){
                is Resource.Success-> {
                    favoriteAdapter.differ.submitList(it.value.products)
                    val boolean = it.value.products.isEmpty()
                    binding.tvNoFav.visible(boolean)
                    binding.ivNoFav.visible(boolean)
                    binding.buttonNoFav.visible(boolean)
                }
                is Resource.Failure ->{
                    handleApiError(it)
                }
            }
        })
    }

    private fun observeRemovedFavList(){
        viewModel.removedFavProductResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    viewModel.getFavoriteProductList(token)
                    Toast.makeText(requireContext(), it.value.msg, Toast.LENGTH_SHORT).show()
                }
                is Resource.Failure -> {
                    handleApiError(it)
                }
            }
        })
    }
}