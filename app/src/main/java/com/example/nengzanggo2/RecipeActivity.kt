package com.example.nengzanggo2

import RecipeAdapter
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.database.Cursor
import android.graphics.BitmapFactory
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
import java.io.File
import java.io.FileNotFoundException


class RecipeActivity : AppCompatActivity() {

    var recipeList = arrayListOf<recipe>()  //recipe 이름 저장하는 배열
    lateinit var gridView: GridView         //recipe 버튼뷰 GridView
    lateinit var recipeName : EditText      //recipe 추가시 입력되는 레시피명
    final val GET_GALLERY_IMAGE : Int = 200 //선택 이미지 상수
    lateinit var imageCute: ImageView       //이미지 선택 아이콘
    val stockHelper = stockDBHelper(this)   //DB접근 변수
    lateinit var uri : Uri                  //이미지 선택한뒤 DB에 저장하기 위한 URI변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recipe_main)
        gridView = findViewById<GridView>(R.id.gridView)

        title="레시피"
        val recipeAdapter = RecipeAdapter(this, recipeList) //레시피 추가 어댑터 선언
        gridView.adapter = recipeAdapter


        //저장된 DB 내용 출력
        val stockDB = stockHelper.readableDatabase
        var cursor = stockDB.rawQuery("SELECT * FROM recipeTBL",null)
        while(cursor.moveToNext()) {
            var n_recipe = recipe(cursor.getString(0), cursor.getString(1))
            recipeList.add(n_recipe)
            recipeAdapter.notifyDataSetChanged()
        }

        //레시피 추가 버튼 리스너
        addRecipeBtn.setOnClickListener {
            //레시피 추가 dialog
            var dialogView = View.inflate(this@RecipeActivity, R.layout.recipe_dialog, null)    
            var dlg = AlertDialog.Builder(this@RecipeActivity)
            val stockWriteDB = stockHelper.writableDatabase

            imageCute = dialogView.findViewById<View>(R.id.imageCute) as ImageView
            imageCute.setOnClickListener(View.OnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = MediaStore.Images.Media.CONTENT_TYPE
                startActivityForResult(intent, GET_GALLERY_IMAGE)
            })
            dlg.setView(dialogView)
            
            //dialog 추가할래 버튼 리스너
            dlg.setNegativeButton("추가할래!") { _, _ ->
                recipeName = dialogView.findViewById<View>(R.id.recipeName) as EditText

                var newRecipeName : String = recipeName.text.toString()
                var newRecipeImage : String = getRealPathFromURI(uri)

                //입력된 변수 DB에 저장
                stockWriteDB.execSQL("INSERT INTO recipeTBL VALUES ( '${newRecipeName}', '${newRecipeImage}' );")
                stockWriteDB.close()


                recipeList.add(recipe(newRecipeName, newRecipeImage))
                recipeAdapter.notifyDataSetChanged()
            }

            dlg.show()
        }


        //하단 메뉴 네비게이션
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
    
    //이미지 출력을 위한 ResultActivity 메소드
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GET_GALLERY_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val selectedImageUri: Uri? = data.data
            imageCute.setImageURI(selectedImageUri)
            uri = data.data!!
        }
    }

    //URI -> 절대경로(String)
    private fun getRealPathFromURI(contentUri: Uri?): String {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = contentResolver.query(contentUri!!, proj, null, null, null)
        cursor?.moveToNext()
        val path: String = cursor!!.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA))
        val uri = Uri.fromFile(File(path))
        cursor.close()
        return path
    }
}


