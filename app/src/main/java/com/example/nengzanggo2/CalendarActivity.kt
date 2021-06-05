package com.example.nengzanggo2

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
//아에이오우
class CalendarActivity : AppCompatActivity(){
    lateinit var calendar : CalendarView
    lateinit var item_name : TextView
    lateinit var duration : EditText
    lateinit var prg : ProgressBar

    val stockHelper = stockDBHelper(this)
    var ingredientList = arrayListOf<ingredient>()
    lateinit var calendarListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
        title = "달력 보기"

        calendar = findViewById(R.id.calendarView)

        //리스트뷰
        calendarListView = findViewById<ListView>(R.id.calendarListView)
        val ingredientAdapter = MainListAdapter(this, ingredientList)
        calendarListView.adapter = ingredientAdapter
        val stockDB = stockHelper.readableDatabase
        var cursor = stockDB.rawQuery("SELECT * FROM stockTBL",null)
        while(cursor.moveToNext())
        {
            var n_ingredient : ingredient = ingredient(cursor.getString(0),cursor.getString(1),cursor.getString(2))
            ingredientList.add(n_ingredient)
            ingredientAdapter.notifyDataSetChanged()
        }



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