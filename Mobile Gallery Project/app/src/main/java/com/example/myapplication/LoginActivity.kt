package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.databinding.ActivityLoginBinding
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUp.setOnClickListener{
            val intent: Intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            if(binding.username.text.isNotEmpty() && binding.password.text.isNotEmpty()){
                val url = AppConfig().IP_SERVER + "/gallery_kel1/auth/login.php"
                val stringRequest = object : StringRequest(
                    Method.POST,url,
                    Response.Listener { response ->
                        try {
                            val jsonObj = JSONObject(response)
                            Toast.makeText(applicationContext,jsonObj.getString("message"),Toast.LENGTH_SHORT).show()
                            if(jsonObj.getString("message") != "Data Salah"){
                                Toast.makeText(applicationContext,"Selamat Datang ${binding.username.text}",Toast.LENGTH_SHORT).show()
                                val intent: Intent = Intent(applicationContext, SellerActivity::class.java)
                                intent.putExtra("username", binding.username.text.toString())
                                startActivity(intent)
                            }
                        }catch (e: Exception){
                            val jsonObj = JSONObject(response)
                            Toast.makeText(applicationContext,jsonObj.getString("message"),Toast.LENGTH_SHORT).show()
                        }
                    },
                    Response.ErrorListener { _ ->
                        Toast.makeText(applicationContext,"Gagal Terhubung",Toast.LENGTH_SHORT).show()
                    }
                ){
                    override fun getParams(): HashMap<String,String>{
                        val params = HashMap<String,String>()
                        params["username"] = binding.username.text.toString()
                        params["password"] = binding.password.text.toString()
                        return params
                    }
                }
                Volley.newRequestQueue(applicationContext).add(stringRequest)
            }else{
                Toast.makeText(applicationContext, "Silahkan Isi Data Terlebih Dahulu", Toast.LENGTH_SHORT).show()
            }
        }
    }
}