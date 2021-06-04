package com.example.nengzanggo2

import RecipeAdapter
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.GridView
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.recipe_main.*


class RecipeActivity : AppCompatActivity() {

    var recipeList = arrayListOf<String>()
    lateinit var gridView: GridView
    lateinit var recipeName : EditText
    final val GET_GALLERY_IMAGE : Int = 200
    lateinit var imageCute: ImageView

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
            var dialogView = View.inflate(this@RecipeActivity, R.layout.recipe_dialog, null)
            var dlg = AlertDialog.Builder(this@RecipeActivity)

            imageCute = dialogView.findViewById<View>(R.id.imageCute) as ImageView
            imageCute.setOnClickListener(View.OnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                startActivityForResult(intent, GET_GALLERY_IMAGE)
            })

            dlg.setView(dialogView)
            dlg.setNegativeButton("추가할래!") { _, _ ->
                recipeName = dialogView.findViewById<View>(R.id.recipeName) as EditText


                stockWriteDB.execSQL("INSERT INTO recipeTBL VALUES ( '${recipeName.text.toString()}' );")
                stockWriteDB.close()

                var newRecipe : String = recipeName.text.toString()
                recipeList.add(newRecipe)
                recipeAdapter.notifyDataSetChanged()

                Toast.makeText(applicationContext, "입력됨", Toast.LENGTH_SHORT).show()
            }

            dlg.show()
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GET_GALLERY_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val selectedImageUri: Uri? = data.data
            imageCute.setImageURI(selectedImageUri)
        }
    }

}


