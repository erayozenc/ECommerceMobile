package com.example.e_commerceapp.ui.home.dialogs

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.e_commerceapp.R
import com.example.e_commerceapp.databinding.DialogBottomSheetSortBinding
import com.example.e_commerceapp.databinding.FragmentHomepageBinding
import com.example.e_commerceapp.responses.products.Product
import com.example.e_commerceapp.ui.home.viewmodel.ProductViewModel
import com.example.e_commerceapp.util.Resource
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.lang.ClassCastException

@AndroidEntryPoint
class SortBottomSheetDialog : BottomSheetDialogFragment() {

    interface SortByListener{
        fun getSortedList(products: List<Product>)
    }

    lateinit var mSortByListener: SortByListener
    private var _binding : DialogBottomSheetSortBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductViewModel by viewModels()
    private var token: String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogBottomSheetSortBinding.inflate(inflater, container, false)
        lifecycleScope.launch {
            token = viewModel.getAuthToken()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.radioButton1.setOnClickListener {
            viewModel.getProductList(token, "desc", "_id", null)
        }
        binding.radioButton2.setOnClickListener {
            viewModel.getProductList(token, "asc", "price", null)
        }
        binding.radioButton3.setOnClickListener {
            viewModel.getProductList(token, "desc", "price", null)
        }
        binding.radioButton4.setOnClickListener {
            viewModel.getProductList(token, "desc", "sold", null)
        }
        binding.radioButton5.setOnClickListener {
            viewModel.getProductList(token, "desc", "createdAt", null)
        }
        binding.radioButton6.setOnClickListener {
            viewModel.getProductList(token, "desc", "quantity", null)
        }

        viewModel.productListResponse.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Success ->{
                    mSortByListener.getSortedList(it.value.products)
                    dismiss()
                }
                is Resource.Failure ->{
                    dismiss()
                }
            }
        })
    }

    override fun onAttach(context: Context) {
        try {
            mSortByListener = targetFragment as SortByListener
        }catch (error : ClassCastException){
            println(error.localizedMessage)
        }
        super.onAttach(context)
    }
}