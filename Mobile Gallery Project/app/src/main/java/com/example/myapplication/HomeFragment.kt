package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import java.lang.Exception
import kotlin.math.log

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

//
    @SuppressLint("ResourceType", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        var lukisanList = arrayListOf<Lukisan>()

        val loading = view.findViewById<ProgressBar>(R.id.loading)
        val buttonAdd = view.findViewById<Button>(R.id.btn_add)
        var recycleView: RecyclerView? = view.findViewById(R.id.recycle_view)

        buttonAdd.setOnClickListener {
            val intent: Intent = Intent(context, AddDataGalleryActivity::class.java)
            startActivity(intent)
        }

        //volley get request
        loading.visibility = View.VISIBLE
        val url = AppConfig().IP_SERVER + "/gallery_kel1/gallery/view_data.php"
        val queue: RequestQueue = Volley.newRequestQueue(context)
        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            //when response is success
            loading.visibility = View.INVISIBLE
            try {
                val data = response.getJSONArray("data")
                for(i in 0..data.length()-1){
                    val jsonObject = data.getJSONObject(i)
                    val image = AppConfig().IP_SERVER + "/lukisan_kel1/" + jsonObject.getString("gambar")
                    val lukisan = Lukisan(
                        jsonObject.getInt("id"),
                        jsonObject.getInt("id_pembelian"),
                        image,
                        jsonObject.getString("judul"),
                        jsonObject.getString("deskripsi"),
                        jsonObject.getString("kategori"),
                        jsonObject.getInt("harga_jual")
                    )
                    lukisanList.add(lukisan)
                }
                recycleView?.layoutManager = LinearLayoutManager(context)
                recycleView?.adapter = LukisanAdapter(lukisanList)

            }catch (e: Exception){
                Log.d("error", e.message.toString())
            }
        }, {error ->
            loading.visibility = View.INVISIBLE
            Log.d("error", error.message.toString())
        })
        queue.add(request)
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}