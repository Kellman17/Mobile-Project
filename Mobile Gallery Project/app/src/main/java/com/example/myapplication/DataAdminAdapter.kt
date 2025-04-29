package com.example.myapplication

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide

class DataAdminAdapter(private val adminlist: ArrayList<DataAdmin>): RecyclerView.Adapter<DataAdminAdapter.ViewHolder>() {
    private lateinit var context: Context
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val id = itemView.findViewById<TextView>(R.id.id)!!
        val email = itemView.findViewById<TextView>(R.id.email)!!
        val username = itemView.findViewById<TextView>(R.id.username)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.data_admin_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return adminlist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val admin = adminlist[position]
        holder.id.text = admin.id.toString()
        holder.email.text = admin.email
        holder.username.text = admin.username
    }

}