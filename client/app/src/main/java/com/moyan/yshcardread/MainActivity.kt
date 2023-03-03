package com.moyan.yshcardread

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import com.bumptech.glide.Glide
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator


class MainActivity : AppCompatActivity() {
    var imageUrls = listOf(
        R.drawable.a11,
        R.drawable.a12,
        R.drawable.a13,
        R.drawable.a14
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //使用默认的图片适配器
        var banner = (bannerLayout1 as Banner<String, BannerImageAdapter<Int>>)
        banner.apply {
            addBannerLifecycleObserver(this@MainActivity)
            setIndicator(CircleIndicator(this@MainActivity))
            setAdapter(object : BannerImageAdapter<Int>(imageUrls) {
                override fun onBindView(holder: BannerImageHolder, data: Int, position: Int, size: Int) {
                    Glide.with(this@MainActivity)
                        .load(data)
                        .into(holder.imageView)
                }
            })
        }
        paper_block.setOnClickListener {
            val intent = Intent(this,WriteActivity::class.java)
            intent.putExtra("label",paper_block.text.toString())
            intent.putExtra("flag","0")
            startActivity(intent)
        }
        paper_box.setOnClickListener {
            val intent = Intent(this,WriteActivity::class.java)
            intent.putExtra("label",paper_box.text.toString())
            intent.putExtra("flag","1")
            startActivity(intent)
        }
        print_block.setOnClickListener {
            val intent = Intent(this,WriteActivity::class.java)
            intent.putExtra("label",print_block.text.toString())
            intent.putExtra("flag","2")
            startActivity(intent)
        }
        knife_model.setOnClickListener {
            val intent = Intent(this,WriteActivity::class.java)
            intent.putExtra("label",knife_model.text.toString())
            intent.putExtra("flag","3")
            startActivity(intent)
        }
        btn_to_display.setOnClickListener {
            val intent = Intent(this,GroupDemoActivity::class.java)
            startActivity(intent)
        }
        btn_to_search.setOnClickListener {
            val intent = Intent(this,SearchActivity::class.java)
            startActivity(intent)
        }
    }
}
//fun Activity.start(cls: Class<*>) {
//    val intent = Intent(this, cls)
//    startActivity(intent)
//}