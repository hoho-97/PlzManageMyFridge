package com.example.nengzanggo2

import RecipeAdapter
import android.animation.ObjectAnimator
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.GridView
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.recipe_main.*
import java.io.File


class RecipeActivity : AppCompatActivity() {

    var recipeList = arrayListOf<recipe>()  //recipe 이름 저장하는 배열
    lateinit var gridView: GridView         //recipe 버튼뷰 GridView
    lateinit var recipeName : EditText      //recipe 추가시 입력되는 레시피명
    final val GET_GALLERY_IMAGE : Int = 200 //선택 이미지 상수
    lateinit var imageSmile: ImageView       //이미지 선택 아이콘
    lateinit var imageDissapointed: ImageView       //이미지 선택 아이콘
    val stockHelper = stockDBHelper(this)   //DB접근 변수
    lateinit var uri : Uri                  //이미지 선택한뒤 DB에 저장하기 위한 URI변수
    private var isFabOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recipe_main)

        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),Context.MODE_PRIVATE)
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),Context.MODE_PRIVATE)
        gridView = findViewById<GridView>(R.id.gridView)

        title="레시피"
        val recipeAdapter = RecipeAdapter(this, recipeList) //레시피 추가 어댑터 선언
        gridView.adapter = recipeAdapter


        //저장된 DB 내용 출력
        val stockDB = stockHelper.readableDatabase
        var intent = intent
        if(intent.hasExtra("recommend")) {
            var recommend_ingredient = intent.getStringExtra("recommend")
            var cursor = stockDB.rawQuery("SELECT * FROM recipeTBL", null)
            while (cursor.moveToNext()) {
                if(cursor.getString(2).contains("$recommend_ingredient")) {
                    var n_recipe = recipe(cursor.getString(0), cursor.getString(1), cursor.getString(2))
                    recipeList.add(n_recipe)
                    recipeAdapter.notifyDataSetChanged()
                }
            }
        }
        else {
            var cursor = stockDB.rawQuery("SELECT * FROM recipeTBL", null)
            while (cursor.moveToNext()) {
                var n_recipe = recipe(cursor.getString(0), cursor.getString(1), cursor.getString(2))
                recipeList.add(n_recipe)
                recipeAdapter.notifyDataSetChanged()
            }
        }

        fabMain.setOnClickListener {
            toggleFab()
        }

        //레시피 추가 버튼 리스너
        addRecipeBtn.setOnClickListener {
            //레시피 추가 dialog
            var dialogView = View.inflate(this@RecipeActivity, R.layout.recipe_dialog, null)    
            var dlg = AlertDialog.Builder(this@RecipeActivity)
            val stockWriteDB = stockHelper.writableDatabase

            imageSmile = dialogView.findViewById<View>(R.id.imageDisappointed) as ImageView
            imageSmile.setOnClickListener(View.OnClickListener {
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
                var newRecipeContent : String = ""
                //**************추가할래 버튼 누르고 다음 재료추가하는 dialog실행!!
                var dialogView_sub = View.inflate(this@RecipeActivity, R.layout.recipe_ingredient_dialog, null)
                var dlg_sub = AlertDialog.Builder(this@RecipeActivity)
                dlg_sub.setView(dialogView_sub)

                var imageSmile = dialogView_sub.findViewById<View>(R.id.imageSmile_) as ImageView
                imageSmile.setImageURI(uri)
                dlg_sub.setNegativeButton("추가할래!") { _, _ ->
                    var recipeIng = arrayOfNulls<EditText>(20)
                    var recipeIngId = arrayOf(R.id.recipeIng1, R.id.recipeIngNum1, R.id.recipeIng2, R.id.recipeIngNum2,
                            R.id.recipeIng3, R.id.recipeIngNum3,R.id.recipeIng4, R.id.recipeIngNum4,
                            R.id.recipeIng5, R.id.recipeIngNum5,R.id.recipeIng6, R.id.recipeIngNum6,
                            R.id.recipeIng7, R.id.recipeIngNum7,R.id.recipeIng8, R.id.recipeIngNum8,
                            R.id.recipeIng9, R.id.recipeIngNum9,R.id.recipeIng10, R.id.recipeIngNum10)
                    for(i in recipeIngId.indices) {
                        recipeIng[i] = dialogView_sub.findViewById<EditText>(recipeIngId[i])

                        var text: String = recipeIng[i]?.text.toString()
                        if(text == "") break
                        newRecipeContent = "$newRecipeContent$text,"
                    }
                    //입력된 변수 DB에 저장
                    stockWriteDB.execSQL("INSERT INTO recipeTBL VALUES ( '${newRecipeName}', '${newRecipeImage}' ,'${newRecipeContent}');")
                    stockWriteDB.close()

                    recipeList.add(recipe(newRecipeName, newRecipeImage, newRecipeContent))
                    recipeAdapter.notifyDataSetChanged()
                }
                dlg_sub.setPositiveButton("취소", null)


                //************레시피 재료 추가 dialog 끝!!
                dlg_sub.show()





            }
            dlg.setPositiveButton("취소", null)
            dlg.show()
        }

        btn_remove.setOnClickListener {
            var dialogView = View.inflate(this@RecipeActivity, R.layout.recipe_dialog_delete, null)
            var dlg = AlertDialog.Builder(this@RecipeActivity)
            val stockWriteDB = stockHelper.writableDatabase

            imageDissapointed = dialogView.findViewById<View>(R.id.imageDisappointed) as ImageView

            dlg.setView(dialogView)

            //dialog 삭제할래 버튼 리스너
            dlg.setNegativeButton("삭제할래!") { _, _ ->
                recipeName = dialogView.findViewById<View>(R.id.recipeName) as EditText

                var newRecipeName : String = recipeName.text.toString()


                stockWriteDB.execSQL("DELETE FROM recipeTBL WHERE RecipeName ='"+newRecipeName+"';")
                stockWriteDB.close()

                recipeList.clear()


                val stockDB = stockHelper.readableDatabase
                var cursor = stockDB.rawQuery("SELECT * FROM recipeTBL",null)
                while(cursor.moveToNext()) {
                    var n_recipe = recipe(cursor.getString(0), cursor.getString(1),cursor.getString(2))
                    recipeList.add(n_recipe)
                    recipeAdapter.notifyDataSetChanged()
                }


            }
            dlg.setPositiveButton("취소", null)
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
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        var menuInflater = menuInflater
        menuInflater.inflate(R.menu.recipe_menu, menu)


        return super.onCreateOptionsMenu(menu);
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.action_recipe_refresh->{
                recipeList.clear()
                val recipeAdapter = RecipeAdapter(this, recipeList)
                gridView.adapter = recipeAdapter
                val stockDB = stockHelper.readableDatabase
                var cursor = stockDB.rawQuery("SELECT * FROM recipeTBL",null)
                while(cursor.moveToNext()) {
                    var n_recipe = recipe(cursor.getString(0), cursor.getString(1),cursor.getString(2))
                    recipeList.add(n_recipe)
                    recipeAdapter.notifyDataSetChanged()
                }
            }
        }
        return false
    }

    //이미지 출력을 위한 ResultActivity 메소드
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GET_GALLERY_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val selectedImageUri: Uri? = data.data
            imageSmile.setImageURI(selectedImageUri)
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

    private fun toggleFab() {

        // 플로팅 액션 버튼 닫기 - 열려있는 플로팅 버튼 집어넣는 애니메이션 세팅
        if (isFabOpen) {
            ObjectAnimator.ofFloat(addRecipeBtn, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(btn_remove, "translationY", 0f).apply { start() }
            fabMain.setImageResource(R.drawable.plus_icon)
        } else {
            ObjectAnimator.ofFloat(addRecipeBtn, "translationY", -150f).apply { start() }
            ObjectAnimator.ofFloat(btn_remove, "translationY", -300f).apply { start() }
            fabMain.setImageResource(R.drawable.x_icon)
        }

        isFabOpen = !isFabOpen

    }
}








