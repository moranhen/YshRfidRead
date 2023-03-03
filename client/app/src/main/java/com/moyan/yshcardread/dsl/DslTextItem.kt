package com.moyan.yshcardread.dsl

import android.content.Intent
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.angcyo.dsladapter.DslAdapterItem
import com.angcyo.dsladapter.DslViewHolder
import com.angcyo.dsladapter.className
import com.angcyo.item.base.LibInitProvider
import com.moyan.yshcardread.ChangeActivity
import com.moyan.yshcardread.R
import com.moyan.yshcardread.data.api.RfidService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class DslTextItem : DslAdapterItem() {
    var id : CharSequence? = null
    var epc: CharSequence? = null
    var name: CharSequence? = null
    var kind :CharSequence? = null
    val scope = MainScope()
    init {
        itemLayoutId = R.layout.display_item
    }

    override fun onItemBind(
        itemHolder: DslViewHolder,
        itemPosition: Int,
        adapterItem: DslAdapterItem,
        payloads: List<Any>
    ) {
        super.onItemBind(itemHolder, itemPosition, adapterItem,payloads)
        itemHolder.v<TextView>(R.id.display_item_name)?.text =
            name ?: "文本位置:$itemPosition ${className()}"
        itemHolder.v<TextView>(R.id.display_item_id)?.text =
            id ?: "文本位置:$itemPosition ${className()}"
        itemHolder.v<TextView>(R.id.display_item_epc)?.text =
            epc ?: "文本位置:$itemPosition ${className()}"
        itemHolder.v<Button>(R.id.display_btnDelete)?.setOnClickListener {
            toast("删除")
            deleteItem(epc.toString())
        }
        itemHolder.v<Button>(R.id.display_btnChange)?.setOnClickListener {
            toast("修改")
            val intent =Intent(it.context, ChangeActivity::class.java)
            intent.putExtra("epc",epc)
            intent.putExtra("name",name)
            intent.putExtra("kind",kind)
            intent.putExtra("flag","0")
            it.context.startActivity(intent)
        }
    }
    fun deleteItem(epc :String) {
        itemDslAdapter?.removeItem(this)
        scope.launch {
            try {
                val result = RfidService.createGithubApi().deleteRepo(epc)
                if(result.total==200){
                    toast("删除成功")
                }else{
                    toast("删除失败")
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
        itemDslAdapter?.notifyDataChanged()
    }
    fun toast(text: CharSequence) {
        Toast.makeText(LibInitProvider.contentProvider, text, Toast.LENGTH_SHORT).show()
    }
}