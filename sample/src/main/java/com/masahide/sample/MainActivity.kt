package com.masahide.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager(this).orientation))
        recyclerView.adapter = RecyclerAdapter(this, mutableListOf(
            "item1\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n",
            "item2\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n",
            "item3\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"
        ))
    }
}
