package com.example.e_commerceapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.e_commerceapp.databinding.ItemCartBinding
import com.example.e_commerceapp.responses.cart.Cart
import com.example.e_commerceapp.responses.products.Product

class CartAdapter : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    var favIdList = listOf<String>()
    inner class CartViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root){

    }

    private val diffCallback = object : DiffUtil.ItemCallback<Cart>(){
        override fun areItemsTheSame(oldItem: Cart, newItem: Cart): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: Cart, newItem: Cart): Boolean {
            return false
        }

    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder(ItemCartBinding.inflate(
            LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val product = differ.currentList[position].product
        ItemCartBinding.bind(holder.itemView).apply {
            Glide.with(holder.itemView).load(product.image).apply(RequestOptions().override(400, 800)).into(itemImage)
            itemBrand.text = "Boleyn Cristal"
            itemName.text = product.name
            itemPrice.text = product.price.toString() + "$"
            itemEstimatedDelivery.text = "Tahmini teslimat: 28 Aralık- 1 Ocak"

            spinnerCount.setSelection(differ.currentList[position].quantity - 1)

            spinnerCount.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    onItemSelectedListener?.let { it(p2, differ.currentList[position],1) }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }

            buttonRemove.setOnClickListener {
                onItemSelectedListener?.let { it(0,differ.currentList[position], 2) }
            }
        }
    }

    override fun getItemCount() = differ.currentList.size

    private var onItemSelectedListener : ((Int, Cart, Int) -> Unit)? = null

    fun setOnItemSelectedListener(listener : (Int, Cart, Int) -> Unit) {
        onItemSelectedListener = listener
    }
}