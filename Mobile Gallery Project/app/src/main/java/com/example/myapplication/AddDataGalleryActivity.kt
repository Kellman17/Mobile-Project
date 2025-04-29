package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.databinding.ActivityAddDataGalleryBinding
import org.json.JSONObject
import java.io.ByteArrayOutputStream

class AddDataGalleryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddDataGalleryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddDataGalleryBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnSubmit.setOnClickListener {
            insertData()
        }
    }

    private fun insertData() {
        val url = AppConfig().IP_SERVER + "/gallery_kel1/gallery/create_data.php"
        val stringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                try {
                    val jsonObj = JSONObject(response)
                    Toast.makeText(this, jsonObj.getString("message"), Toast.LENGTH_SHORT).show()
                    binding.editTextIdPembelian.setText("")
                    binding.editTextDeskripsi.setText("")
                    binding.editTextKategori.setText("")
                    binding.editTextHargaJual.setText("")
                    val intent: Intent = Intent(this, SellerActivity::class.java)
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(this, e.message.toString(), Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { _ ->
                Toast.makeText(this, "Gagal Terhubung", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): HashMap<String, String> {
                val params = HashMap<String, String>()
                params["id"] = binding.editTextIdPembelian.text.toString()
                params["deskripsi"] = binding.editTextDeskripsi.text.toString()
                params["kategori"] = binding.editTextKategori.text.toString()
                params["harga"] = binding.editTextHargaJual.text.toString()
                return params
            }
        }
        Volley.newRequestQueue(this).add(stringRequest)
    }
}