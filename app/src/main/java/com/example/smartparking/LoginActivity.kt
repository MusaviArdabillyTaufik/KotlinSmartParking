package com.example.smartparking

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {
    private val TAG = "PermissionDemo"
    private val RECORD_REQUEST_CODE = 101

    val text = "Hello toast!"
    val duration = Toast.LENGTH_SHORT

    val context = this

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val sharedPreferences = getSharedPreferences("CEKLOGIN", Context.MODE_PRIVATE)
        val stat = sharedPreferences.getString("ROLE", "")
        btnLogin.setOnClickListener {
            var email = email.text.toString()
            var password = password.text.toString()
            postkerserver(email, password)
        }

        setupPermissions()


        registerLog.setOnClickListener {
            val intent = Intent(context, RegistrasiActivity::class.java)

            startActivity(intent)
        }

    }


    private fun postkerserver(data1: String, data2: String) {
        AndroidNetworking.post("https://test-park1ng.000webhostapp.com/postLogin.php")
            .addBodyParameter("email", data1)
            .addBodyParameter("password", data2)
            .setPriority(Priority.MEDIUM).build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {

                    val jsonArray = response.getJSONArray("result")
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        Log.e("_kotlinTitle", jsonObject.optString("role"))
                        var namaLogin = jsonObject.optString("nama_user")
                        var emailLogin = jsonObject.optString("email")
                        var rolelogin = jsonObject.optString("role")
                        var nimlogin = jsonObject.optString("nim")
                        var niklogin = jsonObject.optString("nik")
                        var angkatanlogin = jsonObject.optString("angkatan")
                        var fakultaslogin = jsonObject.optString("fakultas")
                        var telfonlogin = jsonObject.optString("telfon")
                        var alamatlogin = jsonObject.optString("alamat")

                        if (rolelogin == "mahasiswa") {
                            val sharedPreferences =
                                getSharedPreferences("CEKLOGIN", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putString("ROLE", rolelogin)
                            Log.e("_kotlinTitle", jsonObject.optString("nama_user"))
                            Log.e("_kotlinTitle", jsonObject.optString("nim"))
                            Log.e("_kotlinTitle", jsonObject.optString("angkatan"))
                            Log.e("_kotlinTitle", jsonObject.optString("fakultas"))
                            var nama: String = namaLogin.toString()
                            var email: String = emailLogin.toString()
                            var nim: String = nimlogin.toString()
                            var angkatan: String = angkatanlogin.toString()
                            var fakultas: String = fakultaslogin.toString()
                            editor.apply()
                            val inten = Intent(this@LoginActivity, MainActivity::class.java)
                            inten.putExtra("nama", nama)
                            inten.putExtra("email", email)
                            inten.putExtra("nim", nim)
                            inten.putExtra("angkatan", angkatan)
                            inten.putExtra("fakultas", fakultas)
                            startActivity(inten)
                            finish()
                        } else if (rolelogin == "petugas") {
                            val sharedPreferences =
                                getSharedPreferences("CEKLOGIN", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putString("ROLE", rolelogin)
                            Log.e("_kotlinTitle", jsonObject.optString("nama_user"))
                            Log.e("_kotlinTitle", jsonObject.optString("nik"))
                            Log.e("_kotlinTitle", jsonObject.optString("telfon"))
                            Log.e("_kotlinTitle", jsonObject.optString("alamat"))
                            var nama: String = namaLogin.toString()
                            var email: String = emailLogin.toString()
                            var nik: String = niklogin.toString()
                            var telfon: String = telfonlogin.toString()
                            var alamat: String = alamatlogin.toString()

                            editor.apply()
                            val inten = Intent(this@LoginActivity, PetugasActivity::class.java)
                            inten.putExtra("nama", nama)
                            inten.putExtra("email", email)
                            inten.putExtra("nik", nik)
                            inten.putExtra("telfon", telfon)
                            inten.putExtra("alamat", alamat)
                            startActivity(inten)
                            finish()
                        } else if (rolelogin == "admin") {
                            Toast.makeText(applicationContext, "Login lewat WEEBS PLS!", Toast.LENGTH_LONG).show()
                        } else  {
                            Toast.makeText(applicationContext, "Error goblog", Toast.LENGTH_LONG).show()
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

