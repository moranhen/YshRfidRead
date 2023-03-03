package com.moyan.yshcardread

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.moyan.yshcardread.adapter.mutilAdapter
import com.moyan.yshcardread.data.api.RfidPost
import com.moyan.yshcardread.data.api.RfidService
import kotlinx.android.synthetic.main.activity_change.*
import kotlinx.android.synthetic.main.activity_write.*
import kotlinx.coroutines.delay

class ChangeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change)
        change_mTooBar.title = ""
        change_epc.text = intent.getStringExtra("epc")
        val changeName = intent.getStringExtra("name")
        val kind = intent.getStringExtra("kind")!!.toInt()
        val flag = intent.getStringExtra("flag")
        change_sp1.setSelection(kind)
        change_name.setText(changeName)
        change_name.setOnFocusChangeListener{v,hasFocus ->
            if(hasFocus){
                KeyboardktUtils.showKeyboard(v)
            }else{
                KeyboardktUtils.hideKeyboard(v)
            }
        }
        setSupportActionBar(change_mTooBar)  //Toolbar替换系统自带的ActionBar
        change_mTooBar.setNavigationIcon(R.mipmap.back_white) //工具栏左侧导航图标,通常用作返回按钮
        //左侧导航(返回键)点击事件，需要放在setSupportActionBar(mTooBar)之后执行,不然不起作用
        change_mTooBar.setNavigationOnClickListener { onBackPressed() }
        change_comfirm.setOnClickListener {
            val epc = change_epc.text.toString()
            val name =change_name.text.toString()
            val kind = change_sp1.selectedItemPosition.toString()
            val rfidPost = RfidPost(epc,name, kind)
            RfidService.changeRfid(epc,rfidPost){
                if (it?.item!=null) {
                    toast("修改成功")
                    if (flag=="1"){
                        val intent = Intent(this,SearchActivity::class.java)
                        startActivity(intent)
                    }else{
                        val intent = Intent(this,GroupDemoActivity::class.java)
                        startActivity(intent)
                    }
                    // it = newly added user parsed as response
                    // it?.id = newly added user ID
                } else {
                    toast("修改失败")
                }
            }
        }
    }
    fun toast(message:String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()

    }
}