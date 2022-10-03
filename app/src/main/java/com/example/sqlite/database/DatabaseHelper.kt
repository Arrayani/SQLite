package com.example.sqlite.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.sqlite.model.TaskListModel

//gara cara penulisan ga biasa kapital semua buat parameternya,
// gw jadi stuckk

class DatabaseHelper(context: Context): SQLiteOpenHelper(context,DB_NAME,null,DB_VERSION){
    companion object{
        private val DB_NAME = "task"
        private val DB_VERSION = 1    // version tidak pakai tanda kutip, ini integer value nya
        private val TABLE_NAME = "tasklist"
        private val ID = "id"
        private val TASK_NAME = "taskname"
        private val TASK_DETAILS = "taskdetails"
    }

    override fun onCreate(db: SQLiteDatabase?) {
       // val CREATE_TABLE ="CREATE TABLE "+ TABLE_NAME+
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ($ID INTEGER PRIMARY KEY,$TASK_NAME TEXT,$TASK_DETAILS TEXT)"
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(DROP_TABLE)
        onCreate(db)
    }

    fun getAllTask():List<TaskListModel>{
        val tasklist = ArrayList<TaskListModel>()
       // val db = writableDatabase
        val db = readableDatabase
        val selectQuery = "SELECT *FROM $TABLE_NAME"
        val cursor:Cursor = db.rawQuery(selectQuery,null)
        //val cursor =  db.rawQuery(selectQuery,null)
//        if (cursor!=null){
//            if (cursor.moveToFirst()){
//                do {
//                    val tasks = TaskListModel()
////                    tasks.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
//                    //kalo cuma make getColumnIndex ,akan muncul error, maka kita menggunakan getColumnIndexOrThrow
//                    tasks.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
//                    tasks.name = cursor.getString(cursor.getColumnIndex((TABLE_NAME)))
//                    tasks.details = cursor.getString(cursor.getColumnIndex(TASK_DETAILS))
//                    tasklist.add(tasks)
//                }while (cursor.moveToNext())
//            }
//        }
//        cursor.close()
        return tasklist
    }

    //insert
    //kenapa boleean, jika data tidak di masukan, maka akan mereturn false
    //jika data di masukan, maka akan mereturn true
    fun addTask(task : TaskListModel):Boolean{
    val db = this.writableDatabase
        val values = ContentValues()
        values.put(TASK_NAME, task.name)
        values.put(TASK_DETAILS, task.details)
        val _success = db.insert( TABLE_NAME,null,values)
        db.close()
        //return true
        return (Integer.parseInt("$_success")!=-1)
    }
    fun getTask(_id:Int):TaskListModel{
        val tasks = TaskListModel()
        val db = writableDatabase
        val selectQuery = "SELECT *FROM $TABLE_NAME where $ID =  $_id"
        val cursor = db.rawQuery(selectQuery,null)
        cursor.moveToFirst()
        tasks.id = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(ID)))
        tasks.name = cursor.getString(cursor.getColumnIndexOrThrow((TABLE_NAME)))
        tasks.details = cursor.getString(cursor.getColumnIndexOrThrow(TASK_DETAILS))
        cursor.close()
        return tasks
    }
    fun deleteTask(_id: Int):Boolean{
        val db = this.writableDatabase
        val _success :Long = db.delete(TABLE_NAME,ID+"=?", arrayOf(_id.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success")!= -1

    }
    fun update(tasks:TaskListModel):Boolean{
        val db= this.writableDatabase
        val values = ContentValues()
        values.put(TASK_NAME,tasks.name)
        values.put(TASK_DETAILS,tasks.details)
        val _success = db.update(TABLE_NAME,values,ID + "=?", arrayOf(tasks.id.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success")!=-1
    }
}