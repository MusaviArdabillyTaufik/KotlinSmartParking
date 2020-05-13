package com.example.smartparking

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import kotlinx.android.synthetic.main.activity_registrasi.*
import maes.tech.intentanim.CustomIntent
import org.json.JSONArray

class RegistrasiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrasi)

        val f_list = resources.getStringArray(R.array.fakultas_list)
        val spinner = findViewById<Spinner>(R.id.fakultas)
        val arrayadapter = ArrayAdapter(this@RegistrasiActivity, android.R.layout.simple_spinner_item, f_list)
        spinner.adapter = arrayadapter
        spinner.onItemSelectedListener = object  : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                parent?.getItemAtPosition(p2).toString() // njupuk value
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        registerCreate.setOnClickListener {

            val name = nama.text.toString()
            val email = email.text.toString()
            val nim = nim.text.toString()
            val fakultas = spinner.adapter.toString() //Carane nyelok nekene piye pal ?
            val batch = batch.text.toString()
            val password = password.text.toString()

            if (name != "" || email != "" || nim != "" || batch != "" || email != "" ||
                password != ""
            ) {
                if (name != "") {
                    if (email != "") {
                        if (nim != "") {
                                if (batch != "") {
                                    if (password != "") {
                                        postServer(name, email, nim, fakultas, batch, password)
                                        startActivity(Intent(this, LoginActivity::class.java))
                                        Toast.makeText(
                                            applicationContext,
                                            "Registrasi Berhasil !\nSilahkan Login menggunakan akun anda !",
                                            Toast.LENGTH_LONG
                                        )
                                            .show()
                                        CustomIntent.customType(this, "fadein-to-fadeout")
                                        finish()
                                    } else {
                                        Toast.makeText(
                                            applicationContext,
                                            "Password mohon diisi!",
                                            Toast.LENGTH_LONG
                                        )
                                            .show()
                                    }
                                } else {
                                    Toast.makeText(
                                        applicationContext,
                                        "Batch mohon diisi!",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                }
                            } else {
                            Toast.makeText(
                                applicationContext,
                                "NIM mohon diisi!",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }
                    } else {
                        Toast.makeText(applicationContext, "Email mohon diisi!", Toast.LENGTH_LONG)
                            .show()
                    }
                } else {
                    Toast.makeText(applicationContext, "Nama mohon diisi!", Toast.LENGTH_LONG)
                        .show()
                }
            } else {
                Toast.makeText(applicationContext, "Mohon form diisi!", Toast.LENGTH_LONG)
                    .show()
            }

        }

        loginReg.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            CustomIntent.customType(this, "up-to-bottom")
            finish()
        }

    }

    override fun onBackPressed() {
        startActivity(Intent(this, LoginActivity::class.java))
        CustomIntent.customType(this, "up-to-bottom")
        finish()
        return
    }

    fun postServer(
        data1: String,
        data2: String,
        data3: String,
        data4: String,
        data5: String,
        data6: String
    ) {
        Log.i("result", data1 + data2)
        AndroidNetworking.post("http://ubsmart-parking.herokuapp.com/api/mahasiswa/create")
            .addBodyParameter("nama_user", data1)
            .addBodyParameter("email", data2)
            .addBodyParameter("nim", data3)
            .addBodyParameter("fakultas", data4)
            .addBodyParameter("angkatan", data5)
            .addBodyParameter("password", data6)
            .setPriority(Priority.MEDIUM).build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray) {
                }

                override fun onError(anError: ANError?) {
                    Log.i("_err", anError.toString())
                }
            })
    }
}

