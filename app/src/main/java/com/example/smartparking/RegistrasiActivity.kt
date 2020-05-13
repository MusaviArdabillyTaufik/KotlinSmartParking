package com.example.smartparking

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import kotlinx.android.synthetic.main.activity_registrasi.*
import maes.tech.intentanim.CustomIntent
import org.json.JSONArray
import java.util.*
import kotlin.collections.ArrayList

class RegistrasiActivity : AppCompatActivity() {

    companion object{
        lateinit var fakultasIsi:String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrasi)

        val fakultasSpinner = findViewById(R.id.fakultas) as Spinner

        val arrayFak = ArrayList<String>(Arrays.asList(*resources.getStringArray(R.array.fakultas_list)))
        val arrayAdapter = ArrayAdapter(this@RegistrasiActivity, R.layout.array_list_view, arrayFak)
        fakultasSpinner.adapter = arrayAdapter
        fakultasSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                fakultasIsi = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        registerCreate.setOnClickListener {

            val name = nama.text.toString()
            val email = email.text.toString()
            val nim = nim.text.toString()
            val fakultass = fakultasIsi
            val batch = batch.text.toString()
            val password = password.text.toString()

            if (name != "" || email != "" || nim != "" || fakultass != "" || batch != "" || email != "" ||
                password != ""
            ) {
                if (name != "") {
                    if (email != "") {
                        if (nim != "") {
                            if (fakultass != "") {
                                if (batch != "") {
                                    if (password != "") {

                                        postServer(name, email, nim, fakultass, batch, password)

                                        startActivity(Intent(this, LoginActivity::class.java))

                                        Toast.makeText(applicationContext, "Registrasi Berhasil !\nSilahkan Login menggunakan akun anda !", Toast.LENGTH_LONG).show()

                                        CustomIntent.customType(this, "fadein-to-fadeout")

                                        finish()

                                    } else {
                                        Toast.makeText(applicationContext, "Password mohon diisi!", Toast.LENGTH_LONG).show()
                                    }
                                } else {
                                    Toast.makeText(applicationContext, "Batch mohon diisi!", Toast.LENGTH_LONG).show()
                                }
                            } else {
                                Toast.makeText(applicationContext, "Fakultas mohon diisi!", Toast.LENGTH_LONG).show()
                            }
                        } else {
                            Toast.makeText(applicationContext, "NIM mohon diisi!", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(applicationContext, "Email mohon diisi!", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(applicationContext, "Nama mohon diisi!", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(applicationContext, "Mohon form diisi!", Toast.LENGTH_LONG).show()
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
        Log.e("result", data1 +" - "+ data2 +" - "+ data3 +" - "+ data4 +" - "+ data5 +" - "+ data6)
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

