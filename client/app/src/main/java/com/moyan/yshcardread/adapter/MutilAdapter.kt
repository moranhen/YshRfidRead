package com.moyan.yshcardread.adapter

import android.view.Menu
import android.view.View
import android.widget.TextView
import com.moyan.yshcardread.R
import com.moyan.yshcardread.Rfid

import com.ustory.koinsample.Adapter.KAdapterFactory
import com.ustory.koinsample.Adapter.KotlinAdapter
import kotlinx.android.synthetic.main.activity_write.view.*
import kotlinx.android.synthetic.main.write_item.view.*

var mutilAdapter: KotlinAdapter<Rfid> = KAdapterFactory.KAdapter {
    multiLayout {
        layout {
            R.layout.write_item
        }
    }


    bindData { type, vh, data->
        when (type) {
            R.layout.write_item ->{
                vh.itemView.data_id.text = data.id.toString()
                vh.itemView.data_name.text =data.name
                vh.itemView.data_EPC.text = data.epc

            }

        }
    }

}


//var mutilAdapter2: KotlinAdapter<Menu> = KAdapterFactory.KAdapter {
//
//    multiLayout {
//        layout {
//            R.layout.red_layout
//        }
//        layout {
//            R.layout.yellow_layout
//        }
//        layout {
//            R.layout.blue_layout
//        }
//        layout {
//            R.layout.green_layout
//        }
//    }
//
//
//    bindData { type, vh, data ->
//        when (type) {
//            R.layout.red_layout -> vh.itemView.tv_item.text = data.name
//            R.layout.yellow_layout -> vh.itemView.tv_item.text = data.name
//            R.layout.blue_layout -> vh.itemView.tv_item.text = data.name
//            R.layout.green_layout -> vh.itemView.tv_item.text = data.name
//        }
//    }
//}