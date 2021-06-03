package com.example.nengzanggo2

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class animation : AppCompatActivity(){
    lateinit var img : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.animation)

        img = findViewById(R.id.ic_logo)

        img.startAnimation(AnimationUtils.loadAnimation(this, R.anim.splash_in))
        Handler().postDelayed({
            img.startAnimation(AnimationUtils.loadAnimation(this, R.anim.splash_out))
            Handler().postDelayed({
                img.visibility = View.GONE
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            },500)
        },1500)
    }
}