package com.example.e_commerceapp.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.e_commerceapp.R
import com.example.e_commerceapp.databinding.ItemProductBinding
import com.example.e_commerceapp.responses.products.Product

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    var favIdList = listOf<String>()
    inner class ProductViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                val bundle = Bundle().apply {
                    putSerializable("product",differ.currentList[position])
                }
                itemView.findNavController().navigate(R.id.action_homepageFragment_to_productDetailFragment, bundle)
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return false
        }

    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = differ.currentList[position]
        ItemProductBinding.bind(holder.itemView).apply {
            Glide.with(holder.itemView).load(product.image).apply(RequestOptions().override(500, 1000)).into(itemImage)
            tvItemCategory.text = product.category.name
            tvItemName.text = product.name
            tvItemPrice.text = product.price.toString() + "$"

            favIdList.map {
                if (it == product._id)
                    buttonLike.isSelected = true
            }


            buttonLike.setOnClickListener { buttonLike ->
                buttonLike.isSelected = !buttonLike.isSelected
                onItemClickListener?.let { it(product._id, buttonLike.isSelected) }

            }
        }
    }

    override fun getItemCount() = differ.currentList.size

    private var onItemClickListener : ((String, Boolean) -> Unit)? = null

    fun setOnItemClickListener(listener : (String, Boolean) -> Unit) {
        onItemClickListener = listener
    }
}