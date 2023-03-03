package com.moyan.yshcardread

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.angcyo.dsladapter.*
import com.moyan.yshcardread.dsl.AppAdapterStatusItem
import kotlinx.android.synthetic.main.activity_change.*

import kotlinx.android.synthetic.main.activity_display.*
import kotlinx.android.synthetic.main.item_group_head.view.*


abstract class DisplayActivity : AppCompatActivity() {
    lateinit var dslViewHolder: DslViewHolder
    lateinit var refreshLayout: SwipeRefreshLayout
    lateinit var recyclerView: RecyclerView
    /**提供悬停功能*/
    var hoverItemDecoration = HoverItemDecoration()

    /**提供基本的分割线功能*/
    var baseDslItemDecoration = DslItemDecoration()

    var dslAdapter: DslAdapter = DslAdapter().apply {
        dslAdapterStatusItem = AppAdapterStatusItem()
    }

    open fun getBaseLayoutId(): Int = R.layout.activity_display
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getBaseLayoutId())
        dslViewHolder = DslViewHolder(window.decorView)
        initBaseLayout()
        onInitBaseLayoutAfter()
    }


    open fun initBaseLayout() {
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.mipmap.back_white)
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        dslViewHolder.v<RecyclerView>(R.id.display_rec)?.apply {
            recyclerView = this

            addItemDecoration(baseDslItemDecoration)
            hoverItemDecoration.attachToRecyclerView(this)

            layoutManager = object : LinearLayoutManager(context, VERTICAL, false) {
                override fun onLayoutChildren(
                    recycler: RecyclerView.Recycler?,
                    state: RecyclerView.State?
                ) {
                    try {
                        super.onLayoutChildren(recycler, state)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            adapter = dslAdapter
        }

        dslViewHolder.v<SwipeRefreshLayout>(R.id.display_swiper)
            ?.apply {
                refreshLayout = this
                setOnRefreshListener {
                    onRefresh()
                }
            }
    }

    open fun onRefresh() {
        Toast.makeText(this, "刷新", Toast.LENGTH_SHORT).show()
        dslViewHolder.postDelay(1000) {
            refreshLayout.isRefreshing = false
        }
    }

    open fun onInitBaseLayoutAfter() {

    }

    open fun renderAdapter(render: DslAdapter.() -> Unit) {
        dslAdapter.render(action = render)
    }
}
