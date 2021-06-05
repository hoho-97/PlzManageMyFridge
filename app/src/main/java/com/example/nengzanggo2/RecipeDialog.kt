package com.example.nengzanggo2

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.recipe_info.*

class RecipeDialog(context: Context, recipe: String) : BottomSheetDialog(context) {
    lateinit var playBtn : Button
    lateinit var closeImage : ImageView
    lateinit var recipeTitle : TextView
    lateinit var recipeImage : ImageView
    init {

        //R.layout.confirm_bottom_dialog 하단 다이어로그 생성 버튼을 눌렀을 때 보여질 레이아웃
        val view: View = layoutInflater.inflate(R.layout.recipe_info, null)
        setContentView(view)

        recipeTitle = findViewById<TextView>(R.id.recipeTitle)!!
        recipeTitle.text = recipe
        /*recipeImage = findViewById<ImageView>(R.id.recipeImage)!!
        recipeImage.setImageURI(recipeImgPath.toUri())*/
        closeImage = findViewById<ImageView>(R.id.ic_close)!!
        playBtn = findViewById<Button>(R.id.playBtn)!!
        //확인 버튼
        closeImage.setOnClickListener {

            //다이어 로그 숨기기
            dismiss()

        }

    }
}