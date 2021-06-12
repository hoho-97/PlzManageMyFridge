package com.example.nengzanggo2
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.Gravity
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.order.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

//음성인식 부분에 필요한거
private const val SPEECH_REQUEST_CODE = 0

class OrderActivity : AppCompatActivity() {

    val stockHelper = stockDBHelper(this)
    var ingredientList = arrayListOf<ingredient>()
    var ingredientList2 = arrayListOf<ingredient>()
    lateinit var orderListView: ListView
    lateinit var orderListView2 : ListView

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


    override fun onCreate(savedInstanceState: Bundle?) {//
        super.onCreate(savedInstanceState)
        setContentView(R.layout.order)

        title="주문 하기"
        var Edit1 : EditText
        var btnSearch : ImageButton
        var btnMic : ImageButton
        var text : String

        Edit1 = findViewById(R.id.Edit1)
        btnSearch = findViewById(R.id.btnSearch)
        btnMic = findViewById(R.id.btnMic)

        btnSearch.setOnClickListener {
            text = Edit1.text.toString()
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.coupang.com/np/search?component=&q=${text}&channel=user/"))
            startActivity(intent)
        }

        //음성인식 버튼
        btnMic.setOnClickListener {
            displaySpeechRecognizer()
        }


//        orderListView = findViewById<ListView>(R.id.orderListView)
//        val ingredientAdapter = CalendarListAdapter(this, ingredientList) //어댑터 생성
//        orderListView.adapter = ingredientAdapter
//
//        val stockDB = stockHelper.readableDatabase
//        var cursor = stockDB.rawQuery("SELECT * FROM stockTBL",null)
//        while(cursor.moveToNext())
//        {
//            var n_ingredient : ingredient = ingredient(cursor.getString(0),cursor.getString(1),cursor.getString(2))
//
//            var exDateArray = n_ingredient.time.split(".")
//            var exYEAR = exDateArray[0]
//            var exMONTH = exDateArray[1]
//            var exDAY = exDateArray[2]
//
//            var now = LocalDate.now()
//            var nowDate = now.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
//            var nowDateArray = nowDate.split(".")
//            var nowYEAR = nowDateArray[0]
//            var nowMONTH = nowDateArray[1]
//            var nowDAY = nowDateArray[2]
//
//            var remaintime = fewDay(nowYEAR.toInt(),nowMONTH.toInt(),nowDAY.toInt(),exYEAR.toInt(),exMONTH.toInt(),exDAY.toInt())
//
//            if(remaintime<= 3) {
//                ingredientList.add(n_ingredient) //기한 임박 재료리스트에 추가
//                ingredientAdapter.notifyDataSetChanged() //어댑터에 추가 확인시키기(싱크로나이즈)
//            }
//
//        }

        //통합통합!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!111
        orderListView2 = findViewById<ListView>(R.id.orderListView2)
        val OrderListAdapter = OrderListAdapter(this, ingredientList2) //어댑터 생성
        orderListView2.adapter = OrderListAdapter

        val stockDB = stockHelper.readableDatabase
        var cursor2 = stockDB.rawQuery("SELECT * FROM stockTBL",null)
        while(cursor2.moveToNext())
        {
            var n_ingredient : ingredient = ingredient(cursor2.getString(0),cursor2.getString(1),cursor2.getString(2))

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

            if(remaintime<= 3) {
                ingredientList2.add(n_ingredient) //기한 임박 재료리스트에 추가
                OrderListAdapter.notifyDataSetChanged() //어댑터에 추가 확인시키기(싱크로나이즈)
                continue
            }


            //수량쳌
            var name = n_ingredient.name.split("(") // quantity[0]= "이름", quantity[1]= "단위)"
            var quantity = n_ingredient.quantity.toInt()
            //var tmp = quantity[1].split(")") //tmp[0]="단위", tmp[1]=")"

            if(name[1] == "kg)"){
                if(quantity < 1) //1kg 미만일때 리스트 추가
                    ingredientList2.add(n_ingredient)
                    OrderListAdapter.notifyDataSetChanged()
            }
            else if(name[1] == "g)"){
                if(quantity < 300) //300g 미만일때 리스트에 추가
                    ingredientList2.add(n_ingredient)
                    OrderListAdapter.notifyDataSetChanged()
            }
            else if(name[1] == "개)"){
                if(quantity < 40) //40개 미만일때 리스트에 추가
                    ingredientList2.add(n_ingredient)
                    OrderListAdapter.notifyDataSetChanged()
            }

        }

        val bottomNavigation : BottomNavigationView = findViewById(R.id.btm_nav)
        bottomNavigation.selectedItemId =R.id.order
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
        
        
        //음성인식 부분
    }
    private fun displaySpeechRecognizer() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        }
        // Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val spokenText: String? =
                data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).let { results ->
                    results?.get(0)
                }
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.coupang.com/np/search?component=&q=${spokenText}&channel=user/"))
            startActivity(intent)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}