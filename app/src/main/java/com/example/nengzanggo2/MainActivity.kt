package com.example.nengzanggo2
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var img3 : ImageButton
        img3 = findViewById(R.id.imageButton3)

        img3.setOnClickListener{
            var intent = Intent(applicationContext,OrderActivity::class.java)
            startActivity(intent)
        }
    }
}