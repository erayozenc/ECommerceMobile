package com.example.e_commerceapp.ui.home.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_commerceapp.R
import com.example.e_commerceapp.adapter.CartAdapter
import com.example.e_commerceapp.databinding.FragmentBasketBinding
import com.example.e_commerceapp.databinding.FragmentProductDetailBinding
import com.example.e_commerceapp.responses.cart.Cart
import com.example.e_commerceapp.responses.products.Product
import com.example.e_commerceapp.ui.home.viewmodel.ProductViewModel
import com.example.e_commerceapp.util.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyBasketFragment : Fragment(R.layout.fragment_basket) {

    private var _binding : FragmentBasketBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductViewModel by viewModels()
    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding= FragmentBasketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        binding.progressbar.visible(true)

        viewModel.getAllProductsInCart().observe(viewLifecycleOwner, Observer {
            binding.progressbar.visible(false)
            cartAdapter.differ.submitList(it)
            setupUI(it)
            val boolean = it.isEmpty()
            binding.tvNoProduct.visible(boolean)
            binding.ivNoProduct.visible(boolean)
            binding.buttonNoProduct.visible(boolean)

            if (boolean){
                binding.layoutConfirmBasket.visibility = View.INVISIBLE
                binding.recyclerView.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
                binding.line1.visibility = View.INVISIBLE
            }else{
                binding.layoutConfirmBasket.visibility = View.VISIBLE
                binding.recyclerView.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.soft_grey))
                binding.line1.visibility = View.VISIBLE
            }

        })

        cartAdapter.setOnItemSelectedListener { quantity, cart, selection ->
            when(selection){
                //Changing quantity of product
                1->{
                    if (cart.quantity != quantity + 1){
                        cart.quantity = quantity + 1
                        viewModel.updateProductToCart(cart)
                    }
                }
                //Removing product from basket
                2->{
                    viewModel.deleteProductFromCart(cart)
                }
            }

        }
    }

    private fun setupRecyclerView(){
        cartAdapter = CartAdapter()
        binding.recyclerView.apply {
            adapter = cartAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupUI(list : List<Cart>){
        var sum = 0
        if(list.isNotEmpty()){
            list.map { cart ->
                sum += cart.product.price * cart.quantity
            }
        }
        binding.tvProductPrice.text =  "Toplam: " + sum.toString() + "$"
        binding.tvToolbar.text = "Sepetim"
    }
}