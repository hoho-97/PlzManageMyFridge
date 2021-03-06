package com.example.nengzanggo2
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

//ddd
class stockDBHelper(context: Context) : SQLiteOpenHelper(context,"stock",null,1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE stockTBL(sname CHAR(40),squantity CHAR(20),stime CHAR(20));")
        db!!.execSQL("CREATE TABLE recipeTBL(RecipeName CHAR(20), RecipeImage CHAR(200), RecipeContent CHAR(100), RecipeYoutube CHAR(100));")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS stockTBL")
        db!!.execSQL("DROP TABLE IF EXISTS recipeTBL")
        onCreate(db)
    }
}

class MainActivity : AppCompatActivity() {

    var ingredientList = arrayListOf<ingredient>()
    var ingredientList_search = arrayListOf<ingredient>()
    lateinit var dialogView : View
    val stockHelper = stockDBHelper(this)

    lateinit var EditText_name : EditText
    lateinit var EditText_quantity : EditText
    lateinit var EditText_time : EditText
    lateinit var mainListView : ListView
    lateinit var btn_add : FloatingActionButton


    lateinit var spinner_unit : Spinner
    lateinit var spinner_name : Spinner
    lateinit var spinner_name_delete : Spinner
    private var isFabOpen = false

    lateinit var EditText_quan2 : EditText
    lateinit var EditText_recommend : EditText
    lateinit var EditText_search : EditText
    lateinit var fabMain : FloatingActionButton
    lateinit var fabCamera : FloatingActionButton
    lateinit var fabEdit : FloatingActionButton
    lateinit var btn_remove : FloatingActionButton

    lateinit var EditText_quan : EditText
    var ingredientAdapter = MainListAdapter(this, ingredientList)

    var quantity : String? = null
    var selectYear : String? = null
    var selectMonth : String? = null
    var selectDay : String? = null
    var str_name : String? = null
    var str_date : String? = null
    var str_delete : String? = null
    var str_unit : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title="?????? ??????"

        mainListView = findViewById<ListView>(R.id.mainListView)


        mainListView.adapter = ingredientAdapter

        btn_add = findViewById<FloatingActionButton>(R.id.btn_add)


        btn_remove=findViewById(R.id.btn_remove)
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
        // ????????? ?????? ?????? - ?????? ?????? ( + ??????)
        fabMain.setOnClickListener {
            toggleFab()
        }

        // ????????? ?????? ?????? - ?????? ?????? (????????????)
        fabEdit.setOnClickListener {
            dialogView = View.inflate(this@MainActivity,R.layout.stock_dialog_update,null)
            var dlg = AlertDialog.Builder(this@MainActivity)
            EditText_quan2 = dialogView.findViewById<View>(R.id.EditText_quan2) as EditText
            var date_picker = dialogView.findViewById<DatePicker>(R.id.date_picker) as DatePicker
            var str_quan2 :String


            var name_list : ArrayList<String> = arrayListOf() // ????????? ???????????? ?????? ??????

            val stockDB = stockHelper.readableDatabase
            var cursor = stockDB.rawQuery("SELECT * FROM stockTBL",null)
            while(cursor.moveToNext())
            {
                var n_ingredient : ingredient = ingredient(cursor.getString(0),cursor.getString(1),cursor.getString(2))
                name_list.add(cursor.getString(0))
            }
            //????????? ??????
            spinner_name = dialogView.findViewById<Spinner>(R.id.spinner_name) as Spinner
            spinner_name.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, name_list)

            dlg.setView(dialogView)


            date_picker.setOnDateChangedListener{view, year, monthOfYear, dayOfMonth ->
                selectYear=year.toString()
                selectMonth=(monthOfYear+1).toString()
                selectDay=dayOfMonth.toString()
            }



            // ???????????? ?????? ?????????
            dlg.setPositiveButton("????????????"){dialog , which ->
                str_quan2 = EditText_quan2.text.toString()
                val stockDB = stockHelper.writableDatabase
                str_name=spinner_name.selectedItem.toString()
                str_date="'"+selectYear+"."+selectMonth+"."+selectDay+"'"
                println(str_name)
                println(quantity)
                stockDB.execSQL("UPDATE stockTBL SET stime =" + str_date + " WHERE sName = '" + str_name + "';")
                stockDB.execSQL("UPDATE stockTBL SET squantity =" + str_quan2 + " WHERE sName = '" + str_name + "';")

                //???????????? ?????????
                ingredientList.clear()
                val stockDB2 = stockHelper.readableDatabase
                var cursor = stockDB2.rawQuery("SELECT * FROM stockTBL",null)
                while(cursor.moveToNext())
                {
                    var n_ingredient : ingredient = ingredient(cursor.getString(0),cursor.getString(1),cursor.getString(2))
                    ingredientList.add(n_ingredient)
                    ingredientAdapter.notifyDataSetChanged()
                }
                stockDB.close()

                Toast.makeText(applicationContext, "?????????", Toast.LENGTH_SHORT).show()
            }
            dlg.setNegativeButton("??????",null)
            dlg.show()

        }
        // ????????? ?????? ?????? - ?????? ?????? ??????
        btn_remove.setOnClickListener{
            dialogView = View.inflate(this@MainActivity,R.layout.stock_dialog_delete,null)
            var dlg = AlertDialog.Builder(this@MainActivity)
            var name_list : ArrayList<String> = arrayListOf() // ????????? ???????????? ?????? ??????

            val stockDB = stockHelper.readableDatabase
            var cursor = stockDB.rawQuery("SELECT * FROM stockTBL",null)
            while(cursor.moveToNext())
            {
                var n_ingredient : ingredient = ingredient(cursor.getString(0),cursor.getString(1),cursor.getString(2))
                name_list.add(cursor.getString(0))
            }

            //????????? ??????
            spinner_name_delete = dialogView.findViewById<Spinner>(R.id.spinner_name_delete) as Spinner
            spinner_name_delete.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, name_list)

            dlg.setView(dialogView)
            // ???????????? ?????? ?????????
            dlg.setPositiveButton("????????????"){dialog , which ->

                val stockDB = stockHelper.writableDatabase
                str_delete=spinner_name_delete.selectedItem.toString()


                stockDB.execSQL("DELETE FROM stockTBL WHERE sName = '" + str_delete + "';")


                //???????????? ?????????
                ingredientList.clear()
                val stockDB2 = stockHelper.readableDatabase
                var cursor = stockDB2.rawQuery("SELECT * FROM stockTBL",null)
                while(cursor.moveToNext())
                {
                    var n_ingredient : ingredient = ingredient(cursor.getString(0),cursor.getString(1),cursor.getString(2))
                    ingredientList.add(n_ingredient)
                    ingredientAdapter.notifyDataSetChanged()
                }
                stockDB.close()

                Toast.makeText(applicationContext, "?????????", Toast.LENGTH_SHORT).show()
            }
            dlg.setNegativeButton("??????",null)
            dlg.show()
        }

        // ????????? ?????? ?????? - ?????? ?????? ??????
        btn_add.setOnClickListener {
            dialogView = View.inflate(this@MainActivity,R.layout.stock_dialog,null)
            var dlg = AlertDialog.Builder(this@MainActivity)
            EditText_name = dialogView.findViewById<View>(R.id.EditText_name) as EditText
            EditText_quan = dialogView.findViewById<View>(R.id.EditText_quan) as EditText
            var str_quan : String
            var date_picker2 = dialogView.findViewById<DatePicker>(R.id.date_picker2) as DatePicker

            var name_list_unit : ArrayList<String> = arrayListOf("kg","g","???","???","???") // ????????? ???????????? ?????? ??????
            spinner_unit = dialogView.findViewById<Spinner>(R.id.spinner_unit) as Spinner
            spinner_unit.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, name_list_unit)



            date_picker2.setOnDateChangedListener{view, year, monthOfYear, dayOfMonth ->
                selectYear=year.toString()
                selectMonth=(monthOfYear+1).toString()
                selectDay=dayOfMonth.toString()
            }

            dlg.setView(dialogView)

            dlg.setPositiveButton("????????????"){dialog , which ->
                str_quan=EditText_quan.text.toString()
                str_unit=spinner_unit.selectedItem.toString()
                val stockDB = stockHelper.writableDatabase
                str_date=selectYear+"."+selectMonth+"."+selectDay
                stockDB.execSQL("INSERT INTO stockTBL VALUES ( '${EditText_name.text.toString()+"($str_unit)"}' , '${str_quan.toString()}' , '${str_date}' );")
                stockDB.close()

                var n_ingredient : ingredient = ingredient(EditText_name.text.toString()+"($str_unit)",str_quan.toString(), str_date!!)
                ingredientList.add(n_ingredient)
                ingredientAdapter.notifyDataSetChanged()


                Toast.makeText(applicationContext, "?????????", Toast.LENGTH_SHORT).show()
            }
            dlg.setNegativeButton("??????",null)
            dlg.show()


        }

        val bottomNavigation : BottomNavigationView = findViewById(R.id.btm_nav)
        bottomNavigation.selectedItemId =R.id.home
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
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        var menuInflater = menuInflater
        menuInflater.inflate(R.menu.main_menu, menu)


        return super.onCreateOptionsMenu(menu);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.action_search-> {
                dialogView = View.inflate(this@MainActivity, R.layout.search, null)
                var EditText_search = dialogView.findViewById<EditText>(R.id.EditText_search)
                var dlg = AlertDialog.Builder(this@MainActivity)

                var name_list : ArrayList<String> = arrayListOf() // ????????? ???????????? ?????? ??????
                var name_list_search : ArrayList<String> = arrayListOf() // ????????? ???????????? ?????? ??????
                var i=0
                ingredientAdapter = MainListAdapter(this, ingredientList)
                mainListView.adapter = ingredientAdapter

                val stockDB = stockHelper.readableDatabase
                var cursor = stockDB.rawQuery("SELECT * FROM stockTBL",null)
                while(cursor.moveToNext())
                {
                    var n_ingredient : ingredient = ingredient(cursor.getString(0),cursor.getString(1),cursor.getString(2))
                    name_list.add(cursor.getString(0))
                }


                dlg.setView(dialogView)
                dlg.setTitle("?????? ??????")



                dlg.setPositiveButton("????????????"){dialog , which ->
//
                    var cursor2 = stockDB.rawQuery("SELECT * FROM stockTBL",null)
                    while(cursor2.moveToNext())
                    {
                        var name = name_list[i].split("(")
                        var real_name = name[0]
                        if(EditText_search.text.toString().equals(real_name)) //????????? ???????????? ??????????????? ???????????? ?????? ??????
                        {
                            ingredientList.clear()
                            var n_ingredient : ingredient = ingredient(cursor2.getString(0),cursor2.getString(1),cursor2.getString(2))
                            ingredientList.add(n_ingredient)
                            ingredientAdapter.notifyDataSetChanged()
                            break
                        }
                        i++
                    }
                    //???????????? ?????????
                    Toast.makeText(applicationContext, "?????????", Toast.LENGTH_SHORT).show()
                }
                dlg.setNegativeButton("??????",null)
                dlg.show()
            }
            R.id.action_refresh->{
                ingredientList.clear()
                ingredientAdapter = MainListAdapter(this, ingredientList)
                mainListView.adapter = ingredientAdapter
                val stockDB = stockHelper.readableDatabase
                var cursor = stockDB.rawQuery("SELECT * FROM stockTBL",null)
                while(cursor.moveToNext())
                {
                    var n_ingredient : ingredient = ingredient(cursor.getString(0),cursor.getString(1),cursor.getString(2))
                    ingredientList.add(n_ingredient)
                    ingredientAdapter.notifyDataSetChanged()
                }
            }
            R.id.action_recommend->{
                dialogView = View.inflate(this@MainActivity, R.layout.recommend, null)
                var EditText_recommend = dialogView.findViewById<EditText>(R.id.EditText_recommend)
                var dlg = AlertDialog.Builder(this@MainActivity)

                dlg.setView(dialogView)
                dlg.setTitle("????????? ??????")

                dlg.setPositiveButton("?????? ????????? ??????") { dialog, which ->

                    var str_recommend: String
                    str_recommend = EditText_recommend.text.toString()
                    var intent = Intent(applicationContext, RecipeActivity::class.java)
                    intent.putExtra("recommend", str_recommend)
                    startActivity(intent)
                }

                dlg.show()
            }
        }
        return false
    }
    private fun toggleFab() {

        // ????????? ?????? ?????? ?????? - ???????????? ????????? ?????? ???????????? ??????????????? ??????
        if (isFabOpen) {
            ObjectAnimator.ofFloat(fabCamera, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(fabEdit, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(btn_remove, "translationY", 0f).apply { start() }
            fabMain.setImageResource(R.drawable.plus_icon)
        } else {
            ObjectAnimator.ofFloat(fabCamera, "translationY", -150f).apply { start() }
            ObjectAnimator.ofFloat(fabEdit, "translationY", -300f).apply { start() }
            ObjectAnimator.ofFloat(btn_remove, "translationY", -450f).apply { start() }
            fabMain.setImageResource(R.drawable.x_icon)
        }

        isFabOpen = !isFabOpen

    }

}
