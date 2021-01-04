package com.example.e_commerceapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.example.e_commerceapp.databinding.ItemFavoriteBinding
import com.example.e_commerceapp.responses.products.Product

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    inner class FavoriteViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return false
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(ItemFavoriteBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        ))
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val product = differ.currentList[position]
        ItemFavoriteBinding.bind(holder.itemView).apply {
            Glide.with(itemImage.context).load(product.image).into(itemImage)
            itemBrand.text = "Boleyn Cristal"
            itemName.text = product.name
            itemQuantity.text = product.quantity.toString() + "adet stokta"

            itemAddBasket.setOnClickListener {
                onItemClickListener?.let { it(product, 1) }
            }

            buttonRemove.setOnClickListener {
                onItemClickListener?.let { it(product, 2) }
            }

            cardView.setOnClickListener {
                onItemClickListener?.let { it(product, 3) }
            }
        }
    }

    override fun getItemCount() = differ.currentList.size

    private var onItemClickListener : ((Product, Int) -> Unit)? = null

    fun setOnItemClickListener(listener : (Product, Int) -> Unit) {
        onItemClickListener = listener
    }
}