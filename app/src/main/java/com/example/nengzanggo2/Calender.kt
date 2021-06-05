package com.example.nengzanggo2

import android.os.Bundle
import android.widget.CalendarView
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Text

class Calendar : AppCompatActivity(){
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

    }
}