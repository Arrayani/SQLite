package com.example.sqlite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sqlite.adapter.TaskListAdapter
import com.example.sqlite.database.DatabaseHelper
import com.example.sqlite.databinding.ActivityMainBinding
import com.example.sqlite.model.TaskListModel

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var tasklistAdapter : TaskListAdapter ?=null
    var dbHandler : DatabaseHelper ?=null
    var tasklist : List<TaskListModel> = ArrayList<TaskListModel>()
    var linearlayoutManager : LinearLayoutManager ?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btn_add = binding.btAddItems
        val recycler_tasks = binding.rvList

        dbHandler = DatabaseHelper(this)

        fetchlist()
        btn_add.setOnClickListener{
            val i = Intent(applicationContext,add_task::class.java)
            startActivity(i)
        }
    }

    private fun fetchlist(){
        val recycler_tasks = binding.rvList
        tasklist = dbHandler!!.getAllTask()
//        tasklistAdapter = TaskListAdapter(tasklist, this)
        tasklistAdapter = TaskListAdapter(tasklist, applicationContext)
        linearlayoutManager = LinearLayoutManager(applicationContext)
        recycler_tasks.adapter = tasklistAdapter
        tasklistAdapter?.notifyDataSetChanged()


    }
}