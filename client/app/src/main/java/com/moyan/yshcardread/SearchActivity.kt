package com.moyan.yshcardread

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.moyan.yshcardread.data.api.RfidService
import kotlinx.android.synthetic.main.activity_change.*
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {
    val scope = MainScope()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        search_mTooBar.title = ""
        setSupportActionBar(search_mTooBar)  //Toolbar替换系统自带的ActionBar
        search_mTooBar.setNavigationIcon(R.mipmap.back_white) //工具栏左侧导航图标,通常用作返回按钮
        //左侧导航(返回键)点击事件，需要放在setSupportActionBar(mTooBar)之后执行,不然不起作用
        search_mTooBar.setNavigationOnClickListener { onBackPressed() }
        search_epc.setOnFocusChangeListener{v,hasFocus ->
            if(hasFocus){
                KeyboardktUtils.showKeyboard(v)
            }else{
                KeyboardktUtils.hideKeyboard(v)
            }
        }
        search_name_edt.setOnFocusChangeListener{v,hasFocus ->
            if(hasFocus){
                KeyboardktUtils.showKeyboard(v)
            }else{
                KeyboardktUtils.hideKeyboard(v)
            }
        }
        search_name_num.visibility = View.INVISIBLE
        search_kind_num.visibility = View.INVISIBLE
        search_epc_label.visibility = View.INVISIBLE
        search_kind_label.visibility = View.INVISIBLE
        search_btn_delete.visibility= View.INVISIBLE
        search_btn_update.visibility= View.INVISIBLE
        search_name_btn_update.visibility = View.INVISIBLE
        search_name_btn_delete.visibility = View.INVISIBLE
        search_btn.setOnClickListener {
            val epc = search_epc.text.toString()
            scope.launch {
                try {
                    val res = RfidService.createGithubApi().getRepo(epc)
                    if(res != null && !res.items.isEmpty()){
                        search_name_num.visibility = View.VISIBLE
                        search_kind_num.visibility = View.VISIBLE
                        search_btn_delete.visibility= View.VISIBLE
                        search_btn_update.visibility= View.VISIBLE
                         val kindList = arrayListOf<String>("纸板","纸箱","印版","刀模")
                        search_name.text = res.items[0].name
                        search_kind.text = kindList[res.items[0].kind.toInt()]
                        toast("查找成功！！")
                        KeyboardktUtils.hideKeyboard(search_name_edt)
                    }else{
                        toast("查找失败！！")
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }
        search_name_btn.setOnClickListener {
            val name = search_name_edt.text.toString()
            scope.launch {
                try {
                    val res = RfidService.createGithubApi().getRepoByName(name)
                    if(res != null && !res.items.isEmpty()){
                        search_epc_label.visibility = View.VISIBLE
                        search_kind_label.visibility = View.VISIBLE
                        search_name_btn_delete.visibility= View.VISIBLE
                        search_name_btn_update.visibility= View.VISIBLE
                        val kindList = arrayListOf<String>("纸板","纸箱","印版","刀模")
                        search_epc_res.text = res.items[0].uid
                        search_kind_res.text = kindList[res.items[0].kind.toInt()]
                        toast("查找成功！！")
                        KeyboardktUtils.hideKeyboard(search_name_edt)
                    }else{
                        toast("查找失败！！")
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }
        search_name_btn_update.setOnClickListener {
            val intent = Intent(this, ChangeActivity::class.java)
            intent.putExtra("epc",search_epc_res.text.toString())
            intent.putExtra("name",search_name_edt.text.toString())
            val kindNum = when(search_kind_res.text.toString()){
                "纸板" -> "0"
                "纸箱" -> "1"
                "印版" -> "2"
                "刀模" -> "3"
                else -> {""}
            }
            intent.putExtra("kind",kindNum)
            intent.putExtra("flag","1")
            startActivity(intent)
        }
        search_btn_update.setOnClickListener {
            val intent = Intent(this, ChangeActivity::class.java)
            intent.putExtra("epc",search_epc.text.toString())
            intent.putExtra("name",search_name.text.toString())
            val kindNum = when(search_kind.text.toString()){
                "纸板" -> "0"
                "纸箱" -> "1"
                "印版" -> "2"
                "刀模" -> "3"
                else -> {""}
            }
            intent.putExtra("kind",kindNum)
            intent.putExtra("flag","1")
            startActivity(intent)
        }
        search_btn_delete.setOnClickListener {
            deleteRepo(search_epc.text.toString())
        }
        search_name_btn_delete.setOnClickListener {
            deleteRepo(search_epc_res.text.toString())
        }

    }
    private fun deleteRepo(epc:String){
        scope.launch {
            try {
                val result = RfidService.createGithubApi().deleteRepo(epc)
                if(result.total==200){
                    toast("删除成功")
                    onBackPressed()
                }else{
                    toast("删除失败")
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()

        scope.cancel()
    }
    fun toast(message:String){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()

    }
}