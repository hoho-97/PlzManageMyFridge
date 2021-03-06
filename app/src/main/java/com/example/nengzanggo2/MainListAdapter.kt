package com.example.nengzanggo2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

//ㅎㅇ
class MainListAdapter(val context: Context, val ingredientList: ArrayList<ingredient>) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        /* LayoutInflater는 item을 Adapter에서 사용할 View로 부풀려주는(inflate) 역할을 한다. */
        val view: View = LayoutInflater.from(context).inflate(R.layout.main_lv_item, null)

        /* 위에서 생성된 view를 res-layout-main_lv_item.xml 파일의 각 View와 연결하는 과정이다. */
        val ingredient_name = view.findViewById<TextView>(R.id.in_name)
        val ingredient_quantity = view.findViewById<TextView>(R.id.in_exdate)
        val ingredient_time = view.findViewById<TextView>(R.id.in_time)

        /* ArrayList<Dog>의 변수 dog의 이미지와 데이터를 ImageView와 TextView에 담는다. */
        val ingredient = ingredientList[position]
        ingredient_name.text = ingredient.name
        ingredient_quantity.text = ingredient.quantity
        ingredient_time.text = ingredient.time




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