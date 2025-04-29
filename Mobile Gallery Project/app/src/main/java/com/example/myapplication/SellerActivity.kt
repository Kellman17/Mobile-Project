package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.ActivitySellerBinding

class SellerActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySellerBinding
    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        username = intent!!.getStringExtra("username").toString()

        replaceFragment(PembelianFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {
            binding.bottomNavigationView.isItemActiveIndicatorEnabled = true
            when(it.itemId){
                R.id.pembelian -> replaceFragment(PembelianFragment())
                R.id.gallery -> replaceFragment(HomeFragment())
                R.id.penjualan -> replaceFragment(PenjualanFragment())
                R.id.data_admin -> replaceFragment(SellerProfileFragment())
                else -> {

                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        val bundle = Bundle()
        bundle.putString("username", username)
        fragment.arguments = bundle
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}