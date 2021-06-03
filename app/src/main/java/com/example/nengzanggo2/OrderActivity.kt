package com.example.nengzanggo2
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*

class OrderActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.order)

        title="주문!!"
        var Edit1 : EditText
        var image1 : ImageButton

        Edit1 = findViewById(R.id.Edit1)
        image1 = findViewById(R.id.imageButton)

        image1.setOnClickListener {
            var text = Edit1.text.toString()
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.coupang.com/np/search?component=&q=${text}&channel=user/"))
            startActivity(intent)
        }

        val titleArr = arrayOf("된장찌개", "김치찌개", "순두부찌개", "제육볶음")
        var imageList = intArrayOf(
            R.drawable.img1,
            R.drawable.img2,
            R.drawable.img3,
            R.drawable.img4
        )

        var viewFlipper1 : ViewFlipper = findViewById(R.id.viewFlipper1)
        viewFlipper1.inAnimation = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left)
        viewFlipper1.outAnimation = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right)

        for (image in imageList) {
            val imageView = ImageView(this)
            val layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(30,30,30,0)
            layoutParams.gravity= Gravity.CENTER
            imageView.layoutParams=layoutParams
            imageView.setImageResource(image)
            viewFlipper1.addView(imageView)
        }

        /*for (String in titleArr) {
            var tempText : TextView = TextView(this)
            tempText.gravity = Gravity.CENTER_HORIZONTAL
            tempText.text = String
            viewFlipper1.addView(tempText)
        }*/

        viewFlipper1.flipInterval = 2000
        viewFlipper1.startFlipping()
    }
}