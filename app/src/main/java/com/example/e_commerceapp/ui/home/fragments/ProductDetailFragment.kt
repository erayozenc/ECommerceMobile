package com.example.e_commerceapp.ui.home.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.e_commerceapp.R
import com.example.e_commerceapp.adapter.ProductAdapter
import com.example.e_commerceapp.adapter.RelatedProductAdapter
import com.example.e_commerceapp.databinding.FragmentProductDetailBinding
import com.example.e_commerceapp.responses.cart.Cart
import com.example.e_commerceapp.responses.products.Product
import com.example.e_commerceapp.ui.home.viewmodel.ProductViewModel
import com.example.e_commerceapp.util.Resource
import com.example.e_commerceapp.util.handleApiError
import com.example.e_commerceapp.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_product_detail.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailFragment : Fragment(R.layout.fragment_product_detail) {

    private var _binding : FragmentProductDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductViewModel by viewModels()
    private lateinit var relatedProductAdapter: RelatedProductAdapter
    private val args: ProductDetailFragmentArgs by navArgs()
    private lateinit var product: Product

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        product = args.product
        lifecycleScope.launch {
            val authToken = viewModel.getAuthToken()
            viewModel.getProductList(authToken,null,null,null)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressbar.visible(true)
        setupRecyclerView()
        setupUI(product)


        viewModel.productListResponse.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Success ->{
                    println(it.value.products)
                    relatedProductAdapter.differ.submitList(it.value.products)
                }
                is Resource.Failure ->{
                    handleApiError(it)
                }
            }
        })

        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.buttonAddBasket.setOnClickListener {
            product.let {
                val cart = Cart(0, it)
                viewModel.addProductToCart(cart)
            }.apply {
                Toast.makeText(requireContext(), "Ürün başarıyla sepete eklendi.", Toast.LENGTH_SHORT).show()
            }
        }

        relatedProductAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("product",it)
            }
            findNavController().navigate(R.id.action_productDetailFragment_self,bundle)
        }
    }


    private fun setupRecyclerView(){
        relatedProductAdapter = RelatedProductAdapter()
        rvRelatedProducts.apply {
            adapter = relatedProductAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupUI(product : Product){
        Glide.with(requireContext()).load(product.image).apply(RequestOptions().override(600, 550)).into(binding.ivProductImage)
        binding.tvProductCategory.text = product.category.name
        binding.tvProductName.text = product.name
        binding.tvProductPrice.text = product.price.toString() + "$"
        binding.tvProductQuantity.text = product.quantity.toString() + " adet stokta"
        binding.tvProductDescription.text = product.description
        binding.progressbar.visible(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}