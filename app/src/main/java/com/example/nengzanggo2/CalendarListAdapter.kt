package com.example.nengzanggo2

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

//ㅎㅇ
class CalendarListAdapter(val context: Context, val ingredientList: ArrayList<ingredient>) : BaseAdapter() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        /* LayoutInflater는 item을 Adapter에서 사용할 View로 부풀려주는(inflate) 역할을 한다. */
        val view: View = LayoutInflater.from(context).inflate(R.layout.calendar_lv_item, null)

        /* 위에서 생성된 view를 res-layout-calendar_lv_item.xml 파일의 각 View와 연결하는 과정이다. */
        val ingredient_name = view.findViewById<TextView>(R.id.in_name)
        val ingredient_quantity = view.findViewById<TextView>(R.id.in_quantity)//유통기한아이디
        val ingredient_time = view.findViewById<TextView>(R.id.in_time)// 남은일수 아이디

        /* ArrayList<Dog>의 변수 dog의 이미지와 데이터를 ImageView와 TextView에 담는다. */
        val ingredient = ingredientList[position] //db 한 행

        var exDateArray = ingredient.time.split(".")
        var exYEAR = exDateArray[0]
        var exMONTH = exDateArray[1]
        var exDAY = exDateArray[2]
        //
        var now = LocalDate.now()
        var nowDate = now.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
        var nowDateArray = nowDate.split(".")
        var nowYEAR = nowDateArray[0]
        var nowMONTH = nowDateArray[1]
        var nowDAY = nowDateArray[2]

        var remaintime = fewDay(nowYEAR.toInt(),nowMONTH.toInt(),nowDAY.toInt(),exYEAR.toInt(),exMONTH.toInt(),exDAY.toInt())

        ingredient_name.text = ingredient.name // db 한행 중 첫번째(이름) 입력
        ingredient_quantity.text = ingredient.time//유통기한에 유통기한이 들어가고있음
        ingredient_time.text = remaintime.toString()//남은일수에 유통기한이 들어가고있음

        return view
    }

    override fun getItem(position: Int): Any {
        return ingredientList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return ingredientList.size
    }





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
}