package com.example.smartparking


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.EditText
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
import maes.tech.intentanim.CustomIntent
import org.json.JSONArray


class PetugasActivity : AppCompatActivity() {
    private var btn: Button? = null
    var nmaRslt: TextView? = null
    var emlRslt: TextView? = null
    var nikRslt: TextView? = null
    var tlfRslt: TextView? = null
    var almRslt: TextView? = null
    var etplat: EditText? = null
    private val TAG = "PermissionDemo"
    private val RECORD_REQUEST_CODE = 101
    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_petugas)

        setupPermissions()

        tvresult = findViewById<TextView>(R.id.result)
        etplat = findViewById<EditText>(R.id.platNo)
        btn = findViewById<Button>(R.id.btn)
        nmaRslt = findViewById<TextView>(R.id.nmaRslt)
        emlRslt = findViewById<TextView>(R.id.emlRslt)
        nikRslt = findViewById<TextView>(R.id.nikRslt)
        tlfRslt = findViewById<TextView>(R.id.tlfRslt)
        almRslt = findViewById<TextView>(R.id.almRslt)



        var nama = intent.getStringExtra("nama")
        var email = intent.getStringExtra("email")
        var nik = intent.getStringExtra("nik")
        var telfon = intent.getStringExtra("telfon")
        var alamat = intent.getStringExtra("alamat")


        nmaRslt!!.text = "$nama"
        emlRslt!!.text = "$email"
        nikRslt!!.text = "$nik"
        tlfRslt!!.text = "$telfon"
        almRslt!!.text = "$alamat"

        btn!!.setOnClickListener {
            val inten = Intent(this@PetugasActivity, ScanActivity::class.java)
            CustomIntent.customType(this@PetugasActivity, "fadein-to-fadeout")
            startActivity(inten)
        }

        process.setOnClickListener {
            val result = tvresult!!.text.toString()
            val platNo = etplat!!.text.toString()
            if ("$result" != "Hasil") {
                if ("$platNo" != "") {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Parkir Proccess")
                    builder.setMessage("QR Code : $result \nPlat Nomor : $platNo")
                    //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

                    builder.setPositiveButton("MASUK") { dialog, which ->
                        postServer(result, platNo)
                    }
                    builder.setNegativeButton("KELUAR") { dialog, which ->
                        keluarServer(result, platNo)
                    }
                    builder.setNeutralButton("CANCEL") { dialog, which ->

                    }

                    builder.show()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Input plat nomor terlebih dahulu!",
                        Toast.LENGTH_LONG
                    )
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
            Toast.makeText(
                applicationContext,
                "Selamat Tinggal :(",
                Toast.LENGTH_LONG
            ).show()
            CustomIntent.customType(this, "right-to-left")
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
        Log.e("Parkir Masuk", data1 + " - " + data2)
        Toast.makeText(
            applicationContext,
            data1 + " - " + data2 + " berhasil masuk!",
            Toast.LENGTH_LONG
        ).show()
    }

    fun keluarServer(data1: String, data2: String) {
        AndroidNetworking.post("https://test-park1ng.000webhostapp.com/updateLaporan.php")
            .addBodyParameter("qr_code", data1)
            .addBodyParameter("plat_nomor", data2)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray) {
                }

                override fun onError(anError: ANError?) {
                    Log.i("_err", anError.toString())
                }
            })
        Log.e("Parkir Keluar", data1 + " - " + data2)
        Toast.makeText(
            applicationContext,
            data1 + " - " + data2 + " berhasil keluar!",
            Toast.LENGTH_LONG
        ).show()
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

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            CustomIntent.customType(this, "fadein-to-fadeout")
            finish()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Tekan Sekali lagi untuk keluar Aplikasi!", Toast.LENGTH_SHORT).show()

        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }

    companion object {

        var tvresult: TextView? = null
        var nmaRslt: TextView? = null
        var emlRslt: TextView? = null
        var nikRslt: TextView? = null
        var tlfRslt: TextView? = null
        var almRslt: TextView? = null
    }
}
