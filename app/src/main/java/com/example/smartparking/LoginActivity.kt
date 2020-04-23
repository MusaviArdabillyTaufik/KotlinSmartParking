package com.example.smartparking

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.androidnetworking.common.Priority
import org.json.JSONObject
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.androidnetworking.AndroidNetworking
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    private val TAG = "PermissionDemo"
    private val RECORD_REQUEST_CODE = 101

    val context = this
    val ip = "192.168.100.92"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

//        val sharedPreferences = getSharedPreferences("CEKLOGIN", Context.MODE_PRIVATE)
//        val stat = sharedPreferences.getString("STATUS", "")
//        if (stat == "1") {
//            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
//            finish()
//        } else {
//            btnLogin.setOnClickListener {
//                var email = email.text.toString()
//                var password = password.text.toString()
//                Log.i("status",email+password)
//                postkerserver(email, password)
//            }
//        }


        val sharedPreferences = getSharedPreferences("CEKLOGIN", Context.MODE_PRIVATE)
        val stat = sharedPreferences.getString("ROLE", "")
        if (stat == "mahasiswa") {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        } else if (stat == "petugas") {
            startActivity(Intent(this@LoginActivity, PetugasActivity::class.java))
            finish()
        } else {
            btnLogin.setOnClickListener {
                var email = email.text.toString()
                var password = password.text.toString()
                postkerserver(email, password)
            }
        }

        setupPermissions()
        getdariserver()


        registerLog.setOnClickListener {
            val intent = Intent(context, RegistrasiActivity::class.java)

            startActivity(intent)
        }

    }


    fun getdariserver(){
        AndroidNetworking.get("http://$ip/Api/mahasiswa.php")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener{
                override fun onResponse(response: JSONObject) {
                    Log.e("_kotlinResponse", response.toString())

                    val jsonArray = response.getJSONArray("result")
                    for (i in 0 until jsonArray.length()){
                        val jsonObject = jsonArray.getJSONObject(i)
                        Log.e("_kotlinTitle", jsonObject.optString("nama_user"))

                        nama_user.setText(jsonObject.optString("nama_user"))
                    }
                }

                override fun onError(anError: ANError) {
                    Log.i("_err", anError.toString())
                }
            })
    }


//    private fun postkerserver(data1: String, data2: String) {
//        AndroidNetworking.post("http://$ip/Api/postLogin.php")
//            .addBodyParameter("email", data1)
//            .addBodyParameter("password", data2)
//            .setPriority(Priority.MEDIUM).build()
//            .getAsJSONObject(object : JSONObjectRequestListener {
//                override fun onResponse(response: JSONObject) {
//
//                    val jsonArray = response.getJSONArray("result")
//                    for (i in 0 until jsonArray.length()) {
//                        val jsonObject = jsonArray.getJSONObject(i)
//                        Log.e("_kotlinTitle", jsonObject.optString("role"))
//                        var rolelogin = jsonObject.optString("role")
//                        txt1.setText(rolelogin)
//
//                        if (rolelogin == "mahasiswa") {
//                            val sharedPreferences =
//                                getSharedPreferences("CEKLOGIN", Context.MODE_PRIVATE)
//                            val editor = sharedPreferences.edit()
//                            editor.putString("ROLE", rolelogin)
//                            editor.apply()
//                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
//                            finish()
//                        } else if (rolelogin == "petugas") {
//                            val sharedPreferences =
//                                getSharedPreferences("CEKLOGIN", Context.MODE_PRIVATE)
//                            val editor = sharedPreferences.edit()
//                            editor.putString("ROLE", rolelogin)
//                            editor.apply()
//                            startActivity(Intent(this@LoginActivity, PetugasActivity::class.java))
//                            finish()
//                        }
//                    }
//                }
//
//                override fun onError(error: ANError) { // handle error
//                }
//            })
//    }



    fun postkerserver(data1: String, data2: String) {
        AndroidNetworking.post("http://$ip/Api/postLogin.php")
            .addBodyParameter("email", data1)
            .addBodyParameter("pass_kotlin", data2)
            .setPriority(Priority.MEDIUM).build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {

                    val jsonArray = response.getJSONArray("result")
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        Log.e("_kotlinTitle", jsonObject.optString("status"))
                        var statuslogin = jsonObject.optString("status")
                        txt1.setText(statuslogin)
                        if (statuslogin == "1") {
                            val sharedPreferences =
                                getSharedPreferences("CEKLOGIN", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putString("STATUS", statuslogin)
                            editor.apply()
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        }
                    }
                }

                override fun onError(error: ANError) { // handle error
                }
            })
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to record denied")
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.CAMERA),
            RECORD_REQUEST_CODE)
    }
}
