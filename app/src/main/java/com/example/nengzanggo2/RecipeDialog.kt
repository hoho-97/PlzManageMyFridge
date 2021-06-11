package com.example.nengzanggo2

import android.content.Context
import android.graphics.BitmapFactory
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.File
import java.io.FileNotFoundException
import java.util.*


class RecipeDialog(context: Context, recipe: recipe) : BottomSheetDialog(context) {
    lateinit var playBtn : Button
    lateinit var closeImage : ImageView
    lateinit var recipeTitle : TextView
    lateinit var recipeImage : ImageView
    lateinit var contextField : TextView
    init {

        //R.layout.confirm_bottom_dialog 하단 다이어로그 생성 버튼을 눌렀을 때 보여질 레이아웃
        val view: View = layoutInflater.inflate(R.layout.recipe_info, null)
        setContentView(view)

        recipeTitle = findViewById<TextView>(R.id.recipeTitle)!!
        recipeTitle.text = recipe.name
        recipeImage = findViewById<ImageView>(R.id.recipeImage)!!
        setImage(recipe.image)
        contextField = findViewById<TextView>(R.id.recipeContent)!!
        var st = StringTokenizer(recipe.recipeContent, ",")
        while(st.hasMoreTokens()) {
            contextField.text = contextField.text.toString() + st.nextToken() + "\t\t\t\t\t"+st.nextToken() +"\n"
        }

        closeImage = findViewById<ImageView>(R.id.ic_close)!!
        playBtn = findViewById<Button>(R.id.playBtn)!!
        //확인 버튼
        closeImage.setOnClickListener {

            //다이어 로그 숨기기
            dismiss()

        }

    }

    private fun setImage(uri: String) {
        try {
            val file = File(uri)
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            recipeImage.setImageBitmap(bitmap)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }
    
}