package com.example.nengzanggo2
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.animation.ObjectAnimator

class stockDBHelper(context: Context) : SQLiteOpenHelper(context,"stock",null,1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE stockTBL(sname CHAR(40),squantity CHAR(20),stime CHAR(20));")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS stockTBL")
        onCreate(db)
    }
}

class MainActivity : AppCompatActivity() {

    var ingredientList = arrayListOf<ingredient>()
    lateinit var dialogView : View
    val stockHelper = stockDBHelper(this)

    lateinit var EditText_name : EditText
    lateinit var EditText_quantity : EditText
    lateinit var EditText_time : EditText
    lateinit var mainListView : ListView
    lateinit var btn_add : FloatingActionButton

    private var isFabOpen = false


    lateinit var fabMain : FloatingActionButton
    lateinit var fabCamera : FloatingActionButton
    lateinit var fabEdit : FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainListView = findViewById<ListView>(R.id.mainListView)

        val ingredientAdapter = MainListAdapter(this, ingredientList)
        mainListView.adapter = ingredientAdapter

        btn_add = findViewById<FloatingActionButton>(R.id.btn_add)



        fabMain=findViewById<FloatingActionButton>(R.id.fabMain)
        fabCamera=findViewById<FloatingActionButton>(R.id.btn_add)
        fabEdit=findViewById<FloatingActionButton>(R.id.fabEdit)

        val stockDB = stockHelper.readableDatabase
        var cursor = stockDB.rawQuery("SELECT * FROM stockTBL",null)
        while(cursor.moveToNext())
        {
            var n_ingredient : ingredient = ingredient(cursor.getString(0),cursor.getString(1),cursor.getString(2))
            ingredientList.add(n_ingredient)
            ingredientAdapter.notifyDataSetChanged()
        }

        fabMain.setOnClickListener {
            toggleFab()
        }

        // 연필그림
        fabEdit.setOnClickListener {
            Toast.makeText(this, "수정 버튼 클릭!", Toast.LENGTH_SHORT).show()
        }


        btn_add.setOnClickListener {
            dialogView = View.inflate(this@MainActivity,R.layout.stock_dialog,null)
            var dlg = AlertDialog.Builder(this@MainActivity)
            EditText_name = dialogView.findViewById<View>(R.id.EditText_name) as EditText
            EditText_quantity = dialogView.findViewById<View>(R.id.EditText_quantity) as EditText
            EditText_time = dialogView.findViewById<View>(R.id.EditText_time) as EditText


            dlg.setView(dialogView)

            dlg.setPositiveButton("추가할래"){dialog , which ->

                val stockDB = stockHelper.writableDatabase
                stockDB.execSQL("INSERT INTO stockTBL VALUES ( '${EditText_name.text.toString()}' , '${EditText_quantity.text.toString()}' , '${EditText_time.text.toString()}' );")
                stockDB.close()

                var n_ingredient : ingredient = ingredient(EditText_name.text.toString(),EditText_quantity.text.toString(),EditText_time.text.toString())
                ingredientList.add(n_ingredient)
                ingredientAdapter.notifyDataSetChanged()


                Toast.makeText(applicationContext, "입력됨", Toast.LENGTH_SHORT).show()
            }
            dlg.setNegativeButton("취소",null)
            dlg.show()

        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }
    private fun toggleFab() {
        Toast.makeText(this, "메인 플로팅 버튼 클릭 : $isFabOpen", Toast.LENGTH_SHORT).show()

        // 플로팅 액션 버튼 닫기 - 열려있는 플로팅 버튼 집어넣는 애니메이션 세팅
        if (isFabOpen) {
            ObjectAnimator.ofFloat(fabCamera, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(fabEdit, "translationY", 0f).apply { start() }
            fabMain.setImageResource(R.drawable.plus_icon)
        } else {
            ObjectAnimator.ofFloat(fabCamera, "translationY", -200f).apply { start() }
            ObjectAnimator.ofFloat(fabEdit, "translationY", -400f).apply { start() }
            fabMain.setImageResource(R.drawable.x_icon)
        }

        isFabOpen = !isFabOpen

    }
}