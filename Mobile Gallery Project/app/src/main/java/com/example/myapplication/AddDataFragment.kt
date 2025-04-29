package com.example.myapplication

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentTransaction
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddDataFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@Suppress("DEPRECATION")
class AddDataFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var bitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var PICK_IMAGE_REQUEST = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_add_data, container, false)
        var judul: EditText = view.findViewById(R.id.edit_text_judul)
        var deskripsi: EditText = view.findViewById(R.id.edit_text_deskripsi)
        var harga: EditText = view.findViewById(R.id.edit_text_harga)
        var buttonSubmit: Button = view.findViewById(R.id.button_submit)
        var loading: ProgressBar = view.findViewById(R.id.loading)
        var textViewError: TextView = view.findViewById(R.id.text_view_error)
        var imageView: ImageView = view.findViewById(R.id.image_view)


        //bermasalah
        imageView.setOnClickListener {
            pickImageFromGallery()
        }

        buttonSubmit.setOnClickListener {
            insertData()
            if(PICK_IMAGE_REQUEST == 1){
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.replace(R.id.frame_layout, HomeFragment())
                transaction.addToBackStack(null) // Optional: Adds the transaction to the back stack
                transaction.commit()
            }
        }

        return view
    }
    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        PICK_IMAGE_REQUEST = 1
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        try {
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
                val selectedImageUri = data.data
                bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, selectedImageUri)
                val imageView: ImageView = requireView().findViewById(R.id.image_view)
                imageView.setImageBitmap(bitmap)
            }
        }catch (e: Exception){
            Log.d("error_konz", e.message.toString())
        }
    }

    private fun insertData() {
        if (PICK_IMAGE_REQUEST == 1){
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val bytes = byteArrayOutputStream.toByteArray()
            val base64Image = Base64.encodeToString(bytes, Base64.DEFAULT)

            var judul: EditText = requireView().findViewById(R.id.edit_text_judul)
            var deskripsi: EditText = requireView().findViewById(R.id.edit_text_deskripsi)
            var harga: EditText = requireView().findViewById(R.id.edit_text_harga)
            var imageView: ImageView = requireView().findViewById(R.id.image_view)
            val url = AppConfig().IP_SERVER + "/lukisan_kel1/create_data.php"
            val stringRequest = object : StringRequest(Method.POST,url,
                Response.Listener { response ->
                    try {
                        val jsonObj = JSONObject(response)
                        Toast.makeText(context,jsonObj.getString("message"),Toast.LENGTH_SHORT).show()
                        imageView.setImageResource(R.drawable.vector_add_data)
                        judul.setText("")
                        deskripsi.setText("")
                        harga.setText("")
                        PICK_IMAGE_REQUEST = 0
                    }catch (e: Exception){
                        Log.d("Error_image", e.message.toString())
                    }
                },
                Response.ErrorListener { _ ->
                    Toast.makeText(context,"Gagal Terhubung",Toast.LENGTH_SHORT).show()
                }
            ){
                override fun getParams(): HashMap<String,String>{
                    val params = HashMap<String,String>()
                    params["gambar"] = base64Image
                    params["judul"] = judul.text.toString()
                    params["deskripsi"] = deskripsi.text.toString()
                    params["harga"] = harga.text.toString()
                    return params
                }
            }
            Volley.newRequestQueue(context).add(stringRequest)

        }
        else{
            Toast.makeText(context,"Select the image first",Toast.LENGTH_SHORT).show()
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddDataFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddDataFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}