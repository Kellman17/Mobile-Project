package com.example.myapplication

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ActivityEditDataGalleryBinding
import org.json.JSONObject
import java.io.ByteArrayOutputStream

class EditDataGalleryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditDataGalleryBinding
    private lateinit var id: String
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityEditDataGalleryBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        var bundle = intent.getBundleExtra("data")
        id = bundle!!.getString("id")!!
        binding.editTextIdPembelian.setText(bundle?.getString("id_pembelian"))
        binding.editTextDeskripsi.setText(bundle?.getString("deskripsi"))
        binding.editTextKategori.setText(bundle?.getString("kategori"))
        binding.editTextHargaJual.setText(bundle?.getString("harga"))

        binding.btnUpdate.setOnClickListener {
            updateData()
        }
    }

    private fun updateData() {
        val url1: String = AppConfig().IP_SERVER + "/gallery_kel1/gallery/update_data.php"
        val stringRequest = object : StringRequest(Method.POST, url1, Response.Listener { response ->
            try {
                val jsonObj = JSONObject(response)
                Toast.makeText(this, jsonObj.getString("message"), Toast.LENGTH_SHORT).show()
                val intent = Intent(this@EditDataGalleryActivity, SellerActivity::class.java)
                startActivity(intent)
            }catch (e: Exception){
                Toast.makeText(this, e.message.toString(), Toast.LENGTH_SHORT).show()
            }},
            Response.ErrorListener { e ->
                Toast.makeText(this, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): HashMap<String, String> {
                val params = HashMap<String, String>()
                params["id"] = id
                params["id_pembelian"] = binding.editTextIdPembelian.text.toString()
                params["deskripsi"] = binding.editTextDeskripsi.text.toString()
                params["kategori"] = binding.editTextKategori.text.toString()
                params["harga"] = binding.editTextHargaJual.text.toString()
                return params
            }
        }
        Volley.newRequestQueue(this).add(stringRequest)
    }
}