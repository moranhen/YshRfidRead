package com.moyan.yshcardread

import com.angcyo.dsladapter.*
import com.moyan.yshcardread.data.api.RfidService
import com.moyan.yshcardread.dsl.DslTextItem
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 *
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2019/10/16
 * Copyright (c) 2019 ShenZhen O&M Cloud Co., Ltd. All rights reserved.
 */
class GroupDemoActivity : DisplayActivity() {
    companion object {
        val TAG_UPDATE_DATA = "update_data"
    }
    val scope = MainScope()

    override fun onInitBaseLayoutAfter() {
        super.onInitBaseLayoutAfter()

        recyclerView.setPadding(10 * dpi, 0, 10 * dpi, 0)

        dslAdapter.render {
            setAdapterStatus(DslAdapterStatusItem.ADAPTER_STATUS_LOADING)
        }
        fetchData()
    }
    private fun fetchData(){
        scope.launch {
            try {
                val result = RfidService.createGithubApi().getRepos()
                if(result != null && !result.items.isEmpty()){
                    val kindList = arrayListOf<String>("纸板","纸箱","印版","刀模")
                    renderAdapter {
                        setAdapterStatus(DslAdapterStatusItem.ADAPTER_STATUS_NONE)
                        for(i in 0..3) {
                            dslItem(R.layout.item_group_head) {
                                itemIsGroupHead = true //启动分组折叠
                                itemIsHover = false //关闭悬停
                                itemGroups = mutableListOf("group${i + 1}")
                                itemTopInsert = 10 * dpi

                                itemBindOverride = { itemHolder, itemPosition, adapterItem, _ ->
                                    itemHolder.tv(R.id.fold_button)?.text =
                                        if (itemGroupExtend) "折叠" else "展开"

                                    itemHolder.tv(R.id.text_view)?.text = kindList[i]

                                    itemHolder.click(R.id.fold_button) {
                                        itemGroupExtend = !itemGroupExtend
                                    }

                                    itemGroupParams.apply {
                                        if (isOnlyOne()) {
                                            itemHolder.itemView
                                                .setBackgroundResource(R.drawable.shape_group_all)
                                        } else if (isFirstPosition()) {
                                            itemHolder.itemView
                                                .setBackgroundResource(R.drawable.shape_group_header)
                                        } else {
                                            itemHolder.itemView
                                                .setBackgroundColor(resources.getColor(R.color.colorAccent))
                                        }
                                    }
                                }
                            }

                            var j =0

                            result.items.forEach {
                                if (it.kind.toInt() == i){
                                    dslItem(DslTextItem()) {
                                        itemGroups = mutableListOf("group${it.kind.toInt()}")
                                        name = it.name
                                        epc = it.uid
                                        kind = it.kind
                                        id = j.toString()
                                        j+=1
                                        itemBindOverride = { itemHolder, _, _, _ ->
                                            itemGroupParams.apply {
                                                if (isLastPosition()) {
                                                    itemHolder.itemView
                                                        .setBackgroundResource(R.drawable.shape_group_footer)
                                                } else {
                                                    itemHolder.itemView
                                                        .setBackgroundColor(resources.getColor(R.color.default_base_white))
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }
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
    /*var refreshCount = 0

    override fun onRefresh() {
        Toast.makeText(this, "刷新", Toast.LENGTH_SHORT).show()
        dslViewHolder.postDelay(1000) {
            if (refreshCount++ % 2 == 0) {
                refreshLayout.isRefreshing = false
            } else {
                dslAdapter.render {
                    clearItems()
                    refreshLayout.isRefreshing = false
                }
                Toast.makeText(this, "请重试", Toast.LENGTH_SHORT).show()
            }
        }
    }*/
}
