package com.example.nengzanggo2

import RecipeAdapter
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridView
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.recipe_button.*
import kotlinx.android.synthetic.main.recipe_main.*



class RecipeActivity : AppCompatActivity() {

    var recipeList = arrayListOf<String>()
    lateinit var gridView: GridView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recipe_main)
        gridView = findViewById<GridView>(R.id.gridView)
        title="레시피"
        val recipeAdapter = RecipeAdapter(this, recipeList)
        gridView.adapter = recipeAdapter

        val stockHelper = stockDBHelper(this)

        val stockDB = stockHelper.readableDatabase
        var cursor = stockDB.rawQuery("SELECT * FROM recipeTBL",null)
        while(cursor.moveToNext()) {
            var n_recipe = cursor.getString(0)
            recipeList.add(n_recipe)
            recipeAdapter.notifyDataSetChanged()
        }


        addRecipeBtn.setOnClickListener {
            val stockWriteDB = stockHelper.writableDatabase
            stockWriteDB.execSQL("INSERT INTO recipeTBL VALUES ( '${recipeName.text.toString()}' );")
            stockWriteDB.close()

            var newRecipe : String = recipeName.text.toString()
            recipeList.add(newRecipe)
            recipeAdapter.notifyDataSetChanged()

            Toast.makeText(applicationContext, "입력됨", Toast.LENGTH_SHORT).show()

        }

        val bottomNavigation : BottomNavigationView = findViewById(R.id.btm_nav)
        bottomNavigation.selectedItemId =R.id.recipe
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


