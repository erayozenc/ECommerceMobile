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
import com.example.e_commerceapp.databinding.ItemRelatedProductBinding
import com.example.e_commerceapp.responses.products.Product

class RelatedProductAdapter : RecyclerView.Adapter<RelatedProductAdapter.RelatedProductViewHolder>() {

    inner class RelatedProductViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root){

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelatedProductViewHolder {
        return RelatedProductViewHolder(ItemRelatedProductBinding.inflate(
            LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: RelatedProductViewHolder, position: Int) {
        val product = differ.currentList[position]
        ItemRelatedProductBinding.bind(holder.itemView).apply {
            Glide.with(holder.itemView).load(product.image).apply(RequestOptions().override(350, 700)).into(itemImage)
            tvItemCategory.text = product.category.name
            tvItemName.text = product.name
            tvItemPrice.text = product.price.toString() + "$"

            cardView.setOnClickListener{
                onItemClickListener?.let { it(product) }
            }
        }
    }

    override fun getItemCount() = differ.currentList.size

    private var onItemClickListener : ((Product) -> Unit)? = null

    fun setOnItemClickListener(listener : (Product) -> Unit) {
        onItemClickListener = listener
    }
}