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
import com.example.e_commerceapp.databinding.ItemCategoryBinding
import com.example.e_commerceapp.responses.categories.Category

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                val bundle = Bundle().apply {
                    putString("category",differ.currentList[position]._id)
                }
                itemView.findNavController().navigate(R.id.action_categoriesFragment_to_homepageFragment,bundle)
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Category>(){
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return false
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = differ.currentList[position]
        ItemCategoryBinding.bind(holder.itemView).apply {
            Glide.with(itemImage.context).load(category.image).apply(RequestOptions().override(500,1000)).into(itemImage)
            itemName.text = category.name
            itemProductCount.text = "100 ÜRÜNLER"
        }
    }

    override fun getItemCount() = differ.currentList.size

    private var onItemClickListener : ((String, Boolean) -> Unit)? = null

    fun setOnItemClickListener(listener : (String, Boolean) -> Unit) {
        onItemClickListener = listener
    }

}