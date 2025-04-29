package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.databinding.ActivitySignUpBinding
import org.json.JSONObject

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.login.setOnClickListener {
            val intent: Intent = Intent(this@SignUpActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnSignup.setOnClickListener {
            if(binding.email.text.isNotEmpty() && binding.username.text.isNotEmpty() && binding.password.text.isNotEmpty()){
                val url = AppConfig().IP_SERVER + "/gallery_kel1/auth/signup.php"
                val stringRequest = object : StringRequest(
                    Method.POST,url,
                    Response.Listener { response ->
                        try {
                            val jsonObj = JSONObject(response)
                            Toast.makeText(applicationContext,jsonObj.getString("message"), Toast.LENGTH_SHORT).show()
                            val intent: Intent = Intent(applicationContext, LoginActivity::class.java)
                            startActivity(intent)
                        }catch (e: Exception){
                            val jsonObj = JSONObject(response)
                            Toast.makeText(applicationContext,jsonObj.getString("message"), Toast.LENGTH_SHORT).show()
                        }
                    },
                    Response.ErrorListener { _ ->
                        Toast.makeText(applicationContext,"Gagal Terhubung", Toast.LENGTH_SHORT).show()
                    }
                ){
                    override fun getParams(): HashMap<String,String>{
                        val params = HashMap<String,String>()
                        params["email"] = binding.email.text.toString()
                        params["username"] = binding.username.text.toString()
                        params["password"] = binding.password.text.toString()
                        return params
                    }
                }
                Volley.newRequestQueue(applicationContext).add(stringRequest)
            }
            else{
                Toast.makeText(applicationContext, "Silahkan Isi Data Terlebih Dahulu", Toast.LENGTH_SHORT).show()
            }
        }

    }
}