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
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.databinding.FragmentSellerProfileBinding
import java.lang.Exception

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SellerProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SellerProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var username: String
    private lateinit var buttonLogout: Button
    private lateinit var usernameLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_seller_profile, container, false)
        val bundle = Bundle()
//        username = bundle.getString("username").toString()
        username = arguments?.getString("username").toString()
        usernameLogin = view.findViewById(R.id.username)
        if (username != null) {
            usernameLogin.text = "Name : $username"
        }
        buttonLogout = view.findViewById(R.id.button_logout)
        buttonLogout.setOnClickListener {
            val intent = Intent(context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        var adminList = arrayListOf<DataAdmin>()
        var recycleView: RecyclerView? = view.findViewById(R.id.recycler_view)
        val loading = view.findViewById<ProgressBar>(R.id.loading)

        //volley get request
        loading.visibility = View.VISIBLE
        val url = AppConfig().IP_SERVER + "/gallery_kel1/admin/view_data.php"
        val queue: RequestQueue = Volley.newRequestQueue(context)
        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            loading.visibility = View.GONE
            //when response is success
            try {
                val data = response.getJSONArray("data")
                for (i in 0..data.length() - 1) {
                    val jsonObject = data.getJSONObject(i)
                    val admin = DataAdmin(
                        jsonObject.getInt("id"),
                        jsonObject.getString("email"),
                        jsonObject.getString("username"),
                    )
                    adminList.add(admin)
                }
                recycleView?.layoutManager = LinearLayoutManager(context)
                recycleView?.adapter = DataAdminAdapter(adminList)

            } catch (e: Exception) {
                Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }, { error ->
            Toast.makeText(context, error.message.toString(), Toast.LENGTH_SHORT).show()
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
         * @return A new instance of fragment SellerProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SellerProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}