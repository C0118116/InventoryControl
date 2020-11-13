package com.snowdango.inventorycontrol

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.snowdango.inventorycontrol.databinding.ListItemBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.databinding.BindableItem

class OrderActivity :AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.order_list)

        val groupAdapter = GroupAdapter<GroupieViewHolder>()
        findViewById<RecyclerView>(R.id.recyclerView).apply {
            adapter = groupAdapter
            layoutManager = LinearLayoutManager(applicationContext).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
        }
        val items = listOf("りんご", "みかん", "ぶどう", "すいか", "もも", "ばなな")
        items.forEach {
            groupAdapter.add(TextItem(it))
        }
    }
}

class TextItem(private val jsonData: JsonData) : BindableItem<ListItemBinding>(){
    override fun getLayout() : Int = R.layout.list_item
    override fun bind(viewBinding: ListItemBinding, position: Int) {
        viewBinding.itemData = jsonData
    }
}