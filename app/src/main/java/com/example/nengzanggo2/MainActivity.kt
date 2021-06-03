package com.example.nengzanggo2
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AlertDialog

class stockDBHelper(context: Context) : SQLiteOpenHelper(context,"stock",null,1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE stockTBL(sname CHAR(40) PRIMARY KEY,squantity CHAR(20),stime CHAR(20));")
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
    lateinit var btn_add : Button
    lateinit var btn_reset : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_add = findViewById<Button>(R.id.btn_add)
        btn_reset = findViewById<Button>(R.id.btn_reset)
        mainListView = findViewById<ListView>(R.id.mainListView)

        var img3 : ImageButton
        img3 = findViewById(R.id.btnToShoppingActivity)

        img3.setOnClickListener{
            var intent = Intent(applicationContext,OrderActivity::class.java)
            startActivity(intent)
        }

        val ingredientAdapter = MainListAdapter(this, ingredientList)
        mainListView.adapter = ingredientAdapter

        val stockDB = stockHelper.readableDatabase
        var cursor = stockDB.rawQuery("SELECT * FROM stockTBL",null)
        while(cursor.moveToNext())
        {
            var n_ingredient : ingredient = ingredient(cursor.getString(0),cursor.getString(1),cursor.getString(2))
            ingredientList.add(n_ingredient)
            ingredientAdapter.notifyDataSetChanged()
        }

        btn_reset.setOnClickListener {
            val stockDB = stockHelper.writableDatabase
            stockHelper.onUpgrade(stockDB,1,2)
            stockDB.close()

            var intent = getIntent();
            finish();
            startActivity(intent);
            //
        }

        btn_add.setOnClickListener {
            dialogView = View.inflate(this@MainActivity,R.layout.stock_dialog,null)
            var dlg = AlertDialog.Builder(this@MainActivity)
            EditText_name = dialogView.findViewById<View>(R.id.EditText_name) as EditText
            EditText_quantity = dialogView.findViewById<View>(R.id.EditText_quantity) as EditText
            EditText_time = dialogView.findViewById<View>(R.id.EditText_time) as EditText


            dlg.setView(dialogView)

            dlg.setPositiveButton("확인"){dialog , which ->

                val stockDB = stockHelper.writableDatabase
                stockDB.execSQL("INSERT INTO stockTBL VALUES ( '${EditText_name.text.toString()}' , '${EditText_quantity.text.toString()}' , '${EditText_time.text.toString()}' );")
                stockDB.close()

                var n_ingredient : ingredient = ingredient(EditText_name.text.toString(),EditText_quantity.text.toString(),EditText_time.text.toString())
                ingredientList.add(n_ingredient)
                ingredientAdapter.notifyDataSetChanged()


                Toast.makeText(applicationContext, "입력됨", Toast.LENGTH_SHORT).show()
            }
            dlg.show()

        }










    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }
}