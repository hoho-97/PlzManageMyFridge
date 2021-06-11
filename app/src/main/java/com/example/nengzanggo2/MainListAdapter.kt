package com.example.nengzanggo2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

//ㅎㅇ
class MainListAdapter(val context: Context, val ingredientList: ArrayList<ingredient>) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        /* LayoutInflater는 item을 Adapter에서 사용할 View로 부풀려주는(inflate) 역할을 한다. */
        val view: View = LayoutInflater.from(context).inflate(R.layout.main_lv_item, null)

        /* 위에서 생성된 view를 res-layout-main_lv_item.xml 파일의 각 View와 연결하는 과정이다. */
        val ingredient_name = view.findViewById<TextView>(R.id.in_name)
        val ingredient_quantity = view.findViewById<TextView>(R.id.in_quantity)
        val ingredient_time = view.findViewById<TextView>(R.id.in_time)

        /* ArrayList<Dog>의 변수 dog의 이미지와 데이터를 ImageView와 TextView에 담는다. */
        val ingredient = ingredientList[position]
        ingredient_name.text = ingredient.name
        ingredient_quantity.text = ingredient.quantity
        ingredient_time.text = ingredient.time

        view.setOnClickListener{
            var dialogView : View
            dialogView = View.inflate(context,R.layout.recommend,null)
            var dlg = AlertDialog.Builder(context)
            dlg.setView(dialogView)
            // 삭제하기 버튼 클릭시
            dlg.setPositiveButton("추천 레시피 보기") { dialog, which ->
            }
            dlg.setNegativeButton("취소",null)
            dlg.show()
        }



        return view
    }

    override fun getItem(position: Int): Any {
        return ingredientList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return ingredientList.size
    }
}