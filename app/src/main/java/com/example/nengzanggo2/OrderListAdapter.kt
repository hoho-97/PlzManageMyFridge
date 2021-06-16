package com.example.nengzanggo2

import android.content.Context
import android.graphics.Color
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

//ㅎㅇ
class OrderListAdapter(val context: Context, val ingredientList: ArrayList<ingredient>) : BaseAdapter() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        /* LayoutInflater는 item을 Adapter에서 사용할 View로 부풀려주는(inflate) 역할을 한다. */
        val view: View = LayoutInflater.from(context).inflate(R.layout.order_lv_item, null)

        /* 위에서 생성된 view를 res-layout-calendar_lv_item.xml 파일의 각 View와 연결하는 과정이다. */
        val ingredient_name = view.findViewById<TextView>(R.id.in_name)
        val ingredient_exdate = view.findViewById<TextView>(R.id.in_exdate)//유통기한아이디 연결
        val ingredient_time = view.findViewById<TextView>(R.id.in_time)// 남은일수 아이디 연결
        val ingredient_quantity = view.findViewById<TextView>(R.id.in_quantity) //수량 아이디 연결

        var textView_date = view.findViewById<TextView>(R.id.textView_date)
        var textView_unit = view.findViewById<TextView>(R.id.textView_unit)

        textView_date.setTextColor(Color.BLACK)
        textView_unit.setTextColor(Color.BLACK)
        ingredient_exdate.setTextColor(Color.BLACK)
        ingredient_quantity.setTextColor(Color.BLACK)


        /* ArrayList<Dog>의 변수 dog의 이미지와 데이터를 ImageView와 TextView에 담는다. *///////
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
        if(remaintime <= 3){
            textView_date.setTextColor(Color.RED)
            ingredient_time.setTextColor(Color.RED)
        }else{
            textView_date.setTextColor(Color.BLACK)
            ingredient_time.setTextColor(Color.BLACK)
        }




        //ingredient_name.text = ingredient.name // db 한행 중 첫번째(이름) 입력
        var name = ingredient.name.split("(") //name[0]="이름", name[1]="단위)"
        if(name[1] == "kg)"){
            if(ingredient.quantity.toInt() <= 3) { //1kg 미만일때 빨간색
                textView_unit.setTextColor(Color.RED)
                ingredient_quantity.setTextColor(Color.RED)
            }
            textView_unit.setText("kg 남음")
        }
        else if(name[1] == "g)"){
            if(ingredient.quantity.toInt() < 300) { //300g 미만일때 빨간색
                textView_unit.setTextColor(Color.RED)
                ingredient_quantity.setTextColor(Color.RED)
            }
            textView_unit.setText("g 남음")
        }
        else if(name[1] == "개)"){
            if(ingredient.quantity.toInt() < 10) { //40개 미만일때 빨간색
                textView_unit.setTextColor(Color.RED)
                ingredient_quantity.setTextColor(Color.RED)
            }
            textView_unit.setText("개 남음")
        }
        ingredient_name.text = name[0]
        ingredient_exdate.text = ingredient.time// 넘겨받은 db정보 삽입
        ingredient_time.text = remaintime.toString()//계산된 d-day 삽입
        ingredient_quantity.text = ingredient.quantity // 넘겨받은 db정보 삽입



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