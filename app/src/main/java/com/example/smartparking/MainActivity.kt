package com.example.smartparking

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    internal var bitmap: Bitmap? = null
    private var etqr: EditText? = null
    private var iv: ImageView? = null
    private var detailMhs: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        iv = findViewById<ImageView>(R.id.iv)
        etqr = findViewById<EditText>(R.id.etqr)
        detailMhs = findViewById<Button>(R.id.detailMhs)

        var nama = intent.getStringExtra("nama")
        var email = intent.getStringExtra("email")
        var nim = intent.getStringExtra("nim")
        var angkatan = intent.getStringExtra("angkatan")
        var fakultas = intent.getStringExtra("fakultas")

        etqr!!.setText("$nim")
        etqr!!.isEnabled = false

        detailMhs!!.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("$nama")
            builder.setMessage(" Email : $email \n NIM : $nim \n Angkatan : $angkatan \n Fakultas : $fakultas")
            //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

            builder.setNegativeButton(android.R.string.no) { dialog, which ->

            }

            builder.show()
        }

        logout.setOnClickListener{
            val sharedPreferences=getSharedPreferences("CEKLOGIN", Context.MODE_PRIVATE)
            val editor=sharedPreferences.edit()

            editor.putString("ROLE"," ")
            editor.apply()

            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }

        if (etqr!!.text.toString().trim { it <= ' ' }.length == 0) {
            Toast.makeText(this@MainActivity, "Enter String!", Toast.LENGTH_SHORT).show()
        } else {
            try {
                bitmap = TextToImageEncode(etqr!!.text.toString())
                iv!!.setImageBitmap(bitmap)
            } catch (e: WriterException) {
                e.printStackTrace()
            }
        }
    }


    @Throws(WriterException::class)
    private fun TextToImageEncode(Value: String): Bitmap? {
        val bitMatrix: BitMatrix
        try {
            bitMatrix = MultiFormatWriter().encode(
                Value,
                BarcodeFormat.QR_CODE,
                QRcodeWidth, QRcodeWidth, null
            )

        } catch (Illegalargumentexception: IllegalArgumentException) {

            return null
        }

        val bitMatrixWidth = bitMatrix.width

        val bitMatrixHeight = bitMatrix.height

        val pixels = IntArray(bitMatrixWidth * bitMatrixHeight)

        for (y in 0 until bitMatrixHeight) {
            val offset = y * bitMatrixWidth

            for (x in 0 until bitMatrixWidth) {

                pixels[offset + x] = if (bitMatrix.get(x, y))
                    resources.getColor(R.color.black)
                else
                    resources.getColor(R.color.white)
            }
        }
        val bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444)

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight)
        return bitmap
    }

    companion object {

        val QRcodeWidth = 500
        private val IMAGE_DIRECTORY = "/QRcodeDemonuts"
    }


}
