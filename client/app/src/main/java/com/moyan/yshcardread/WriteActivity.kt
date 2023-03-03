package com.moyan.yshcardread

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.moyan.yshcardread.adapter.mutilAdapter
import com.moyan.yshcardread.data.api.RfidPost
import com.moyan.yshcardread.data.api.RfidService
import kotlinx.android.synthetic.main.activity_write.*
import kotlinx.android.synthetic.main.write_item.view.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class WriteActivity : AppCompatActivity() {
    var datas =ArrayList<Rfid>()
    var hideKey =true
    val scope = MainScope()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write)
        val flag = intent.getStringExtra("flag")
        mTooBar.title= ""
        mt_text.text =intent.getStringExtra("label")  //标题栏文字
        setSupportActionBar(mTooBar)  //Toolbar替换系统自带的ActionBar
        mTooBar.setNavigationIcon(R.mipmap.back_white) //工具栏左侧导航图标,通常用作返回按钮
        write_epc.setOnFocusChangeListener{v,hasFocus ->
            if(hasFocus){
                KeyboardktUtils.showKeyboard(v)
            }
        }
        write_edit.setOnFocusChangeListener{v,hasFocus ->
            if(hasFocus){
                KeyboardktUtils.showKeyboard(v)
            }
        }
        write_num.setOnFocusChangeListener{v,hasFocus ->
            if(hasFocus){
                KeyboardktUtils.showKeyboard(v)
            }
        }

        //左侧导航(返回键)点击事件，需要放在setSupportActionBar(mTooBar)之后执行,不然不起作用
        mTooBar.setNavigationOnClickListener { onBackPressed() }
        write_clear.setOnClickListener { write_edit.text.clear() }
        write_num_btn.setOnClickListener {
            if (write_num.text.toString() != ""){
                write_total_num.text = write_num.text
            }else{
                toast("请先填写要写入的数量")
            }
        }

        write_confirm.setOnClickListener {
            val epc = write_epc.text.toString()
            val name = write_edit.text.toString()
            var confirm = true
            if(epc.equals("")){
                toast("请先录入芯片！")
                confirm=false
            }
            if (name.equals("")){
                toast("请输入芯片的名称！")
                confirm=false
            }else{
                when (flag){
                    "0" -> {
                        if (name[0] != 'L'){
                            toast("请输入正确的订单号类型！")
                            confirm=false
                        }
                    }
                    "1" ->{
                        if (name[0] != 'P'){
                            toast("请输入正确的订单号类型！")
                            confirm=false
                        }
                    }
                    "2"->{
                        if(name.substring(0,2) != "YB"){
                            toast("请输入正确的订单号类型！")
                            confirm=false
                        }
                    }
                    "3"->{
                        if(name.substring(0,2) != "DM"){
                            toast("请输入正确的订单号类型！")
                            confirm=false
                        }
                    }
                }
            }

            if (confirm){
                val id = write_had_num.text.toString().toInt()
                val kind = flag!!
                val rfidPost = RfidPost(epc,name, kind)
                val rfid =Rfid(id,name,epc,kind)
                scope.launch {
                    try {
                        val res = RfidService.createGithubApi().getRepo(epc)
                        if(res != null && !res.items.isEmpty()){
                            RfidService.changeRfid(epc,rfidPost){
                                if (it?.item!=null) {
                                    toast("修改成功")
                                    write_edit.text.clear()
                                    write_epc.text.clear()
                                    write_had_num.text =(id+1).toString()
                                    datas.add(rfid)
                                    mutilAdapter.addData(rfid)
                                    mutilAdapter.notifyDataSetChanged()
                                } else {
                                    toast("修改失败")
                                }
                            }
                        }else{
                            RfidService.addRfid(rfidPost){
                                if (it?.item!=null) {
                                    toast("写入成功")
                                    write_edit.text.clear()
                                    write_epc.text.clear()
                                    write_had_num.text =(id+1).toString()
                                    datas.add(rfid)
                                    mutilAdapter.addData(rfid)
                                    mutilAdapter.notifyDataSetChanged()
                                    // it = newly added user parsed as response
                                    // it?.id = newly added user ID
                                } else {
                                    toast("写入失败")
                                }
                            }
                        }
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }
            }
//            val rfid = Rfid(, name,)
        }
        write_epc_clear.setOnClickListener { write_epc.text.clear() }
        write_rec.layoutManager = LinearLayoutManager(this)
        mutilAdapter.data { datas }
        mutilAdapter into write_rec
        write_submit.setOnClickListener {
            if(write_had_num.text.toString()!= write_total_num.text.toString()){
                toast("芯片未完全写入，请检查！！")
            }else{
                onBackPressed()
            }
        }
    }
    fun toast(message:String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()

    }

    override fun onDestroy() {
        super.onDestroy()

        scope.cancel()
    }
}
