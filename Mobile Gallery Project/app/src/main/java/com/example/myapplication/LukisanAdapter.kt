package com.example.myapplication

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.manager.SupportRequestManagerFragment
import org.json.JSONObject

class LukisanAdapter(private val lukisanList: ArrayList<Lukisan>): RecyclerView.Adapter<LukisanAdapter.ViewHolder>() {
    private lateinit var context: Context
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val image = itemView.findViewById<ImageView>(R.id.image_view)
        val judul = itemView.findViewById<TextView>(R.id.textView)
        val deskripsi = itemView.findViewById<TextView>(R.id.textView2)
        val kategori = itemView.findViewById<TextView>(R.id.textView3)
        val hargaJual = itemView.findViewById<TextView>(R.id.textView4)
        val buttonEdit: Button = itemView.findViewById(R.id.buttonEdit)
        val buttonDelete: Button = itemView.findViewById(R.id.buttonDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.lukisan_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return lukisanList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lukisan = lukisanList[position]
        Glide.with(context).load(lukisan.image).into(holder.image)
        holder.judul.text = lukisan.judul
        holder.deskripsi.text = lukisan.deskripsi
        holder.kategori.text = lukisan.kategori
        holder.hargaJual.text = "Rp. " + lukisan.harga_jual
        //button
        holder.buttonEdit.setOnClickListener {
            val intent: Intent = Intent(context, EditDataGalleryActivity::class.java)
            val bundle: Bundle = Bundle()
            bundle.putString("id", lukisan.id.toString())
            bundle.putString("id_pembelian", lukisan.id_pembelian.toString())
            bundle.putString("judul", holder.judul.text.toString())
            bundle.putString("deskripsi", holder.deskripsi.text.toString())
            bundle.putString("kategori", holder.kategori.text.toString())
            bundle.putString("harga", holder.hargaJual.text.toString().substring(4, holder.hargaJual.text.length))
            intent.putExtra("data", bundle)
            context.startActivity(intent)
        }
        holder.buttonDelete.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle("Lukisan " + lukisan.id.toString())
            builder.setMessage("Apakah Anda Yakin Ingin Menghapus " + holder.judul.text + " ?")

            builder.setNegativeButton("Batal"){_,_->}

            builder.setPositiveButton("Hapus") { _,_ ->
                deleteData(lukisan.id.toString())
            }
            builder.show()
        }

    }

    private fun deleteData(id: String){
        val url: String = AppConfig().IP_SERVER + "/gallery_kel1/gallery/delete_data.php"
        try {
            val stringRequest = object : StringRequest(
                Method.POST,url,
                Response.Listener { _ ->
                    Toast.makeText(context, "Berhasil Hapus Lukisan", Toast.LENGTH_SHORT).show()
                    val intent: Intent = Intent(context, SellerActivity::class.java)
                    context.startActivity(intent)
                },
                Response.ErrorListener { _ ->
                    Toast.makeText(context,"Gagal Terhubung",Toast.LENGTH_SHORT).show()
                }
            ){
                override fun getParams(): HashMap<String,String>{
                    val params = HashMap<String,String>()
                    params["id"] = id
                    return params
                }
            }
            Volley.newRequestQueue(context).add(stringRequest)
        }catch (e: Exception){
            Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}