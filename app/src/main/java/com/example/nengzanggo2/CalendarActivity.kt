package com.example.nengzanggo2

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

//아에이오우
class CalendarActivity : AppCompatActivity(){
    lateinit var calendar : CalendarView
    lateinit var item_name : TextView
    lateinit var duration : EditText
    lateinit var prg : ProgressBar

    val stockHelper = stockDBHelper(this)
    var ingredientList = arrayListOf<ingredient>()
    lateinit var calendarListView: ListView

    //D-Day계산 메소드드
    fun fewDay(beginDayYear:Int, beginDayMonth:Int, beginDayDate:Int, lastDayYear:Int, lastDayMonth:Int, lastDayDate:Int):Long{ //두 날짜간 차이 구하기
        //첫번째날
        val beginDay= Calendar.getInstance().apply{
            set(Calendar.YEAR, beginDayYear)
            set(Calendar.MONTH, beginDayMonth)
            set(Calendar.DAY_OF_MONTH, beginDayDate)
        }.timeInMillis

        //두번째날
        val lastDay= Calendar.getInstance().apply{
            set(Calendar.YEAR, lastDayYear)
            set(Calendar.MONTH, lastDayMonth)
            set(Calendar.DAY_OF_MONTH, lastDayDate)
        }.timeInMillis

        val fewDay = getIgnoredTimeDays(lastDay) - getIgnoredTimeDays(beginDay)
        return fewDay / (24*60*60*1000)
    }
    fun getIgnoredTimeDays(time: Long): Long{
        return Calendar.getInstance().apply{
            timeInMillis = time
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE,0)
            set(Calendar.SECOND,0)
            set(Calendar.MILLISECOND,0)
        }.timeInMillis
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
        title = "달력 보기"

        calendar = findViewById(R.id.calendarView)

        //리스트뷰
        calendarListView = findViewById<ListView>(R.id.calListView) //
        val ingredientAdapter = CalendarListAdapter(this, ingredientList) //어댑터 생성
        calendarListView.adapter = ingredientAdapter

        val stockDB = stockHelper.readableDatabase
        var cursor = stockDB.rawQuery("SELECT * FROM stockTBL",null)
        while(cursor.moveToNext())
        {
            var n_ingredient : ingredient = ingredient(cursor.getString(0),cursor.getString(1),cursor.getString(2))

            var exDateArray = n_ingredient.time.split(".")
            var exYEAR = exDateArray[0]
            var exMONTH = exDateArray[1]
            var exDAY = exDateArray[2]

            var now = LocalDate.now()
            var nowDate = now.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
            var nowDateArray = nowDate.split(".")
            var nowYEAR = nowDateArray[0]
            var nowMONTH = nowDateArray[1]
            var nowDAY = nowDateArray[2]

            var remaintime = fewDay(nowYEAR.toInt(),nowMONTH.toInt(),nowDAY.toInt(),exYEAR.toInt(),exMONTH.toInt(),exDAY.toInt())

            if(remaintime<= 7) {
                ingredientList.add(n_ingredient) //기한 임박 재료리스트에 추가
                ingredientAdapter.notifyDataSetChanged() //어댑터에 추가 확인시키기(싱크로나이즈)
            }

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