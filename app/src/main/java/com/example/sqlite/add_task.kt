package com.example.sqlite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.sqlite.database.DatabaseHelper
import com.example.sqlite.databinding.ActivityAddTaskBinding
import com.example.sqlite.model.TaskListModel

class add_task : AppCompatActivity() {
    lateinit var binding:ActivityAddTaskBinding
    var dbHandler : DatabaseHelper ?=null
    var isEditMode : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val btn_save = binding.btnSave
        val btn_del  = binding.btnDel
        val et_name = binding.etName
        val et_details = binding.etDetails

        dbHandler = DatabaseHelper(this)

        //baru tau gw, ternyata intent dengan Intent itu beda
        //intent itu sama dengan getIntent()
       // if(getIntent() !=null && intent.getStringExtra("mode")=="E"){
        if(intent !=null && intent.getStringExtra("mode")=="E"){
        //update data
            isEditMode = true
            btn_save.text = "Update Data"
            btn_save.visibility = View.VISIBLE
            //di bagian sini harus di mengerti
            val tasks : TaskListModel = dbHandler!!.getTask(intent.getIntExtra("id",0))
            et_name.setText(tasks.name )
            et_details.setText(tasks.details )

        }else{
            // insert data
            isEditMode = false
            btn_save.text = "Save Data"
            btn_del.visibility= View.GONE
        }
        btn_save.setOnClickListener{
            var success : Boolean = false
            val tasks : TaskListModel = TaskListModel()
            if(isEditMode){
                //update
                tasks.id = intent.getIntExtra("id",0)
                tasks.name = et_name.text.toString()
                tasks.details = et_details.text.toString()

                success = dbHandler?.update(tasks) as Boolean
            }else{
                //insert
                tasks.name = et_name.text.toString()
                tasks.details= et_details.text.toString()
                success = dbHandler?.addTask(tasks)  as Boolean
            }

            if (success){ // jadi jika berhasil menginsert data, maka kembali ke main activity
                val i = Intent(applicationContext,MainActivity::class.java)
                startActivity(i)
                finish()
            }else{
                Toast.makeText(applicationContext,"Error execute database",Toast.LENGTH_LONG).show()
            }
        }
        btn_del.setOnClickListener{
            val dialog  = AlertDialog.Builder(this).setTitle("info").setMessage("Click yes if you want to delete")
                .setPositiveButton("YES") { dialog, i ->
                    val success = dbHandler?.deleteTask(intent.getIntExtra("id", 0)) as Boolean
                    if (success)
                        finish()
                    dialog.dismiss()
                }
                .setNegativeButton("NO") { dialog, i ->
                    dialog.dismiss()
                }
            dialog.show()
        }

    }
}