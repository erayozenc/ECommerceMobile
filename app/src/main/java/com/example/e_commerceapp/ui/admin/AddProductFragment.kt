package com.example.e_commerceapp.ui.admin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.e_commerceapp.R
import com.example.e_commerceapp.databinding.FragmentAddProductBinding
import com.example.e_commerceapp.responses.categories.Category
import com.example.e_commerceapp.responses.products.upload.UploadRequestBody
import com.example.e_commerceapp.ui.home.viewmodel.CategoryViewModel
import com.example.e_commerceapp.ui.home.viewmodel.ProductViewModel
import com.example.e_commerceapp.util.*
import com.example.e_commerceapp.util.Constants.REQUEST_CODE_IMAGE_PICK
import com.example.e_commerceapp.util.Constants.REQUEST_CODE_PERMISSION
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*
import java.util.jar.Manifest

@AndroidEntryPoint
class AddProductFragment : Fragment(R.layout.fragment_add_product), EasyPermissions.PermissionCallbacks, UploadRequestBody.UploadCallback {

    private var _binding : FragmentAddProductBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductViewModel by viewModels()
    private val categoryViewModel : CategoryViewModel by viewModels()
    private var token: String? = null
    private var image: Uri? = null
    private var categories = listOf<Category>()
    private var category: Category? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAddProductBinding.inflate(inflater, container, false)
        lifecycleScope.launch {
            token = viewModel.getAuthToken()
            categoryViewModel.getCategoryList(token)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeCategories()
        observeUploadResponse()

       binding.spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
           override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
               category = categories[p2]
           }

           override fun onNothingSelected(p0: AdapterView<*>?) {
               TODO("Not yet implemented")
           }

       }

        binding.buttonAddProduct.setOnClickListener {
            uploadProduct(
                    binding.etProductName.text.toString(),
                    binding.etProductDescription.text.toString(),
                    binding.etProductPrice.text.toString(),
                    category?._id,
                    binding.etProductQuantity.text.toString()
            )
        }

        binding.ivProductImage.setOnClickListener {
            requestPermission()
        }


    }

    private fun uploadProduct(
        name : String?,
        description: String?,
        price: String?,
        category: String?,
        quantity: String?
    ) {
        if (image == null){
            binding.layoutAddProduct.snackbar("Ürün için bir fotoğraf seçmeniz gerekmektedir!")
            return
        }

        if (name == null){
            binding.layoutAddProduct.snackbar("Ürün için bir isim girmeniz gerekmektedir!")
            return
        }

        if (description == null){
            binding.layoutAddProduct.snackbar("Ürün için bir açıklama girmeniz gerekmektedir!")
            return
        }

        if (price == null){
            binding.layoutAddProduct.snackbar("Ürün için bir fiyat girmeniz gerekmektedir!")
            return
        }

        if (category == null){
            binding.layoutAddProduct.snackbar("Ürün için bir kategori seçmeniz gerekmektedir!")
            return
        }

        if (quantity == null){
            binding.layoutAddProduct.snackbar("Ürün için bir adet belirlemeniz gerekmektedir!")
            return
        }


        val parcelFileDescriptor =
            requireActivity().contentResolver.openFileDescriptor(image!!,"r", null) ?: return
        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(requireActivity().cacheDir, requireActivity().contentResolver.getFileName(image!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        binding.progressbar.progress = 0

        val body = UploadRequestBody(file, "image", this)

        viewModel.saveProduct(
            token,
            MultipartBody.Part.createFormData("image", file.name, body),
            name.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            description.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            price.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            category.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            quantity.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        )
    }

    private fun observeCategories(){
        categoryViewModel.categoryListResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when(it){
                is Resource.Success ->{
                    categories = it.value.categories
                    val list = arrayListOf<String>()
                    categories.map { category ->
                        list.add(category.name)
                    }
                    setupSpinner(list)
                }
                is Resource.Failure->{
                    handleApiError(it)
                }
            }
        })
    }

    private fun observeUploadResponse(){
        viewModel.uploadResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Resource.Success -> {
                    if (it.value.product == null)
                        Toast.makeText(requireContext(), it.value.error, Toast.LENGTH_SHORT).show()
                    else
                        Toast.makeText(requireContext(), "Ürün başarıyla database'e eklendi.", Toast.LENGTH_SHORT).show()
                }
                is Resource.Failure -> {
                    handleApiError(it)
                }
            }
        })
    }

    private fun setupSpinner(list : List<String>){
        val spinnerAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,list)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = spinnerAdapter
        spinnerAdapter.notifyDataSetChanged()

    }

    private fun requestPermission(){
        if (PermissionUtil.hasPermission(requireContext()))
            pickImageFromGallery()
        EasyPermissions.requestPermissions(
                this,
                "You need to accept external storage permission to upload a photo!",
                REQUEST_CODE_PERMISSION,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        pickImageFromGallery()
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_IMAGE_PICK)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms))
            AppSettingsDialog.Builder(this).build().show()
        else
            requestPermission()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_IMAGE_PICK && data != null){
            image = data.data
            binding.ivProductImage.setImageURI(image)
        }
    }

    override fun onProgressUpdate(percentage: Int) {
        binding.progressbar.progress = percentage
    }
}