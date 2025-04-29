package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ActivityEditBinding
import org.json.JSONObject
import org.w3c.dom.Text
import java.io.ByteArrayOutputStream
import java.io.IOException

class EditActivity : AppCompatActivity() {
    private lateinit var id: String
    private lateinit var binding: ActivityEditBinding
    var PICK_IMAGE_REQUEST = 0
    private lateinit var bitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityEditBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        imagePick()

        updateData()

    }

    private fun updateData() {
        var bundle = intent.extras
        if (bundle != null) {
            id = bundle!!.getString("id")!!
            binding.editTextJudul.setText(bundle?.getString("judul"))
            binding.editTextDeskripsi.setText(bundle?.getString("deskripsi"))
            binding.editTextHarga.setText(bundle?.getString("harga"))
            Glide.with(this)
                .load(bundle?.getString("image"))
                .override(1500, 1500)
                .placeholder(R.drawable.ic_person) // Optional: Placeholder image while loading
                .error(R.drawable.ic_person) // Optional: Image to display in case of an error
                .into(binding.imageView)
                binding.buttonUpdate.setOnClickListener {
                    if (PICK_IMAGE_REQUEST == 1) {
                        val byteArrayOutputStream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                        val bytes = byteArrayOutputStream.toByteArray()
                        val base64Image = Base64.encodeToString(bytes, Base64.DEFAULT)
                        val url1: String =
                            AppConfig().IP_SERVER + "/lukisan_kel1/update_data.php"
                        val stringRequest = object : StringRequest(Method.POST, url1,
                            Response.Listener { response ->
                                try {
                                    val jsonObj = JSONObject(response)
                                    Toast.makeText(this, jsonObj.getString("message"), Toast.LENGTH_SHORT).show()
                                    PICK_IMAGE_REQUEST = 0
                                    val intent = Intent(this@EditActivity, SellerActivity::class.java)
                                    startActivity(intent)
                                }catch (e: Exception){
                                    Toast.makeText(this, e.message.toString(), Toast.LENGTH_SHORT).show()
                                }
                            },
                            Response.ErrorListener { e ->
                                Toast.makeText(this, e.message.toString(), Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            override fun getParams(): HashMap<String, String> {
                                val params = HashMap<String, String>()
                                params["id"] = id
                                params["gambar"] = base64Image
                                params["judul"] = binding.editTextJudul.text.toString()
                                params["deskripsi"] = binding.editTextDeskripsi.text.toString()
                                params["harga"] = binding.editTextHarga.text.toString()
                                return params
                            }
                        }
                        Volley.newRequestQueue(this).add(stringRequest)
                    }
                    else {
                        val url2: String = AppConfig().IP_SERVER + "/lukisan_kel1/update_dataWithoutImage.php"
                        val stringRequest = object : StringRequest(Method.POST,url2,
                            Response.Listener { response ->
                                val jsonObj = JSONObject(response)
                                Toast.makeText(this,jsonObj.getString("message"),Toast.LENGTH_SHORT).show()
                                PICK_IMAGE_REQUEST = 0
                                val intent = Intent(this@EditActivity, SellerActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                            },
                            Response.ErrorListener { _ ->
                                Toast.makeText(this,"Gagal Terhubung",Toast.LENGTH_SHORT).show()
                            }
                        ){
                            override fun getParams(): HashMap<String,String>{
                                val params = HashMap<String,String>()
                                params["id"] = id
                                params["judul"] = binding.editTextJudul.text.toString()
                                params["deskripsi"] = binding.editTextDeskripsi.text.toString()
                                params["harga"] = binding.editTextHarga.text.toString()
                                return params
                            }
                        }
                        Volley.newRequestQueue(this).add(stringRequest)
                    }
                }
            }
        }



    private fun imagePick() {
        val activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data!!
                val uri = data.data
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    binding.imageView.setImageBitmap(bitmap)
                    PICK_IMAGE_REQUEST = 1
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                PICK_IMAGE_REQUEST = 0
            }
        }
        binding.imageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            activityResultLauncher.launch(intent)
        }
    }
}
