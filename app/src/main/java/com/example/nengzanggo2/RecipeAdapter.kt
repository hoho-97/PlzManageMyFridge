import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.drawable.toDrawable
import androidx.core.graphics.scale
import androidx.core.view.marginBottom
import com.example.nengzanggo2.R
import com.example.nengzanggo2.RecipeDialog
import com.example.nengzanggo2.recipe
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.File
import java.io.FileNotFoundException


class RecipeAdapter(val context: Context, val recipeList: ArrayList<recipe>) : BaseAdapter() {

    lateinit var dialogRecipe : BottomSheetDialog

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        /* LayoutInflater는 item을 Adapter에서 사용할 View로 부풀려주는(inflate) 역할을 한다. */
        val view: View = LayoutInflater.from(context).inflate(R.layout.recipe_button, null)

        /* 위에서 생성된 view를 res-layout-main_lv_item.xml 파일의 각 View와 연결하는 과정이다. */
        var recipeBtn = view.findViewById<Button>(R.id.recipeBtn)


        /* ArrayList<Dog>의 변수 dog의 이미지와 데이터를 ImageView와 TextView에 담는다. */
        val recipe = recipeList[position]
        recipeBtn.text = recipe.name
        try {
            val file = File(recipe.image)
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)

            recipeBtn.setCompoundDrawablesWithIntrinsicBounds(null, BitmapDrawable(bitmap.scale(750,500,false)),null,null)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        view.setOnClickListener {
            /*//alertdialog 부분
            var dialogView = View.inflate(context, R.layout.recipe_info, null)
            var dlg = AlertDialog.Builder(context)
            dlg.setTitle(recipe)
            dlg.setView(dialogView)
            dlg.setNegativeButton("닫기", null)
            dlg.show()*/
            val recipeDialog = RecipeDialog(context, recipe)
            recipeDialog.show()
        }

        return view
    }

    override fun getItem(position: Int): Any {
        return recipeList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return recipeList.size
    }

}