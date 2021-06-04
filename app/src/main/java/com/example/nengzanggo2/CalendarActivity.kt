package com.example.nengzanggo2

import android.content.Intent
import android.os.Bundle
import android.widget.CalendarView
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.w3c.dom.Text

class CalendarActivity : AppCompatActivity(){
    lateinit var calendar : CalendarView
    lateinit var item_name : TextView
    lateinit var duration : EditText
    lateinit var prg : ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.order)

        calendar = findViewById(R.id.calendarView)
        item_name = findViewById(R.id.textView3)
        duration = findViewById(R.id.edittext)
        prg = findViewById(R.id.progressBar)


        val bottomNavigation : BottomNavigationView = findViewById(R.id.btm_nav)
        bottomNavigation.selectedItemId =R.id.calendar
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                R.id.order -> {
                    startActivity(Intent(this, OrderActivity::class.java))
                    finish()
                }
                R.id.recipe -> {
                    startActivity(Intent(this,RecipeActivity::class.java))
                    finish()
                }
                R.id.calendar -> {
                    startActivity(Intent(this,CalendarActivity::class.java))
                    finish()
                }
            }
            true
        }
    }
}