package com.example.smartparking


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import kotlinx.android.synthetic.main.activity_petugas.*
import org.json.JSONArray

val web = "192.168.100.92"


class PetugasActivity : AppCompatActivity() {
    private var btn: Button? = null
    private val TAG = "PermissionDemo"
    private val RECORD_REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_petugas)

        setupPermissions()


        var nama = intent.getStringExtra("nama")
        var email = intent.getStringExtra("email")
        var nik = intent.getStringExtra("nik")
        var telfon = intent.getStringExtra("telfon")
        var alamat = intent.getStringExtra("alamat")

        nmaRslt.text = "$nama"
        emlRslt.text = "$email"
        nikRslt.text = "$nik"
        tlfRslt.text = "$telfon"
        almRslt.text = "$alamat"

        tvresult = findViewById<TextView>(R.id.result)


        btn = findViewById<Button>(R.id.btn)

        btn!!.setOnClickListener {
            val intent = Intent(this@PetugasActivity, ScanActivity::class.java)
            startActivity(intent)
        }


        process.setOnClickListener {
            val result = result.text.toString()
            val platNo = platNo.text.toString()
            if (result != "hasil") {
                if (platNo != null) {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Parkir Proccess")
                    builder.setMessage("QR Code : $result \nPlat Nomor : $platNo")
                    //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

                    builder.setPositiveButton("MASUK") { dialog, which ->
                        postServer(result, platNo)
                        Log.i("Parkir Masuk", result + platNo)
                        startActivity(Intent(this, PetugasActivity::class.java))
                    }
                    builder.setNegativeButton("KELUAR") { dialog, which ->
                        keluarServer(result, platNo)
                        Log.i("Parkir Masuk", result + platNo)
                        startActivity(Intent(this, PetugasActivity::class.java))
                    }
                    builder.setNeutralButton("CANCEL") { dialog, which ->

                    }

                    builder.show()
                } else {
                    Toast.makeText(applicationContext, "Input plat nomor ya cok", Toast.LENGTH_LONG)
                        .show()
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Silahkan scan terlebih dahulu!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }


        logoutP.setOnClickListener {
            val sharedPreferences = getSharedPreferences("CEKLOGIN", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            editor.putString("ROLE", " ")
            editor.apply()

            startActivity(Intent(this@PetugasActivity, LoginActivity::class.java))
            finish()
        }

    }


    fun postServer(data1: String, data2: String) {
        AndroidNetworking.post("https://test-park1ng.000webhostapp.com/createLaporan.php")
            .addBodyParameter("qr_code", data1)
            .addBodyParameter("plat_nomor", data2)
            .setPriority(Priority.MEDIUM).build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray) {
                }

                override fun onError(anError: ANError?) {
                    Log.i("_err", anError.toString())
                }
            })
        Log.i("Parkir Masuk", data1 + data2)
    }

    fun keluarServer(data1: String, data2: String) {
        AndroidNetworking.post("https://test-park1ng.000webhostapp.com/updateLaporan.php")
            .addBodyParameter("qr_code", data1)
            .addBodyParameter("plat_nomor", data2)
            .setPriority(Priority.MEDIUM).build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray) {
                }

                override fun onError(anError: ANError?) {
                    Log.i("_err", anError.toString())
                }
            })
        Log.i("Parkir Keluar", data1 + data2)
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



    companion object {

        var tvresult: TextView? = null
    }
}


