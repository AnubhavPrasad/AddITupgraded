package com.example.listmaker

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast

val DATABASE_NAME = "MYDB"
val TABLE_NAME = "List"
val COL_DATE = "date"
val COL_ID = "id"
val COL_VALUE = "Value"
val TABLE_NAME2="Monthwise"
val COL_VALUE2="Monthvalue"
val COL_MONTH="Month"
val COL_ID2="id2"
class DatabaseHelper(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 4) {
    override fun onCreate(db: SQLiteDatabase?) {
        val create1 =
            "CREATE TABLE $TABLE_NAME($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,$COL_VALUE VARCHAR(255),$COL_DATE VARCHAR(255));"
        db?.execSQL(create1)
        val create2="CREATE TABLE $TABLE_NAME2 ($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,$COL_MONTH VARCHAR(255),$COL_VALUE2 VARCHAR(255));"
        db?.execSQL(create2)
        Log.i("Inside", "Oncreate")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    fun insertdata(data: Data) {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(COL_DATE, data.date)
        cv.put(COL_VALUE, data.value)
        val res = db.insert(TABLE_NAME, null, cv)
        if (res == (-1).toLong()) {
            Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show()
        }
    }

    fun readdata(): MutableList<Data> {
        val list = mutableListOf<Data>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val res = db.rawQuery(query, null)
        if (res.moveToFirst()) {
            do {
                val data = Data()
                data.id = res.getString(res.getColumnIndex(COL_ID)).toInt()
                data.value = res.getString(res.getColumnIndex(COL_VALUE))
                data.date = res.getString(res.getColumnIndex(COL_DATE))
                list.add(data)
            } while (res.moveToNext())
            res.close()
            db.close()
        }
        return list

    }

    fun deletedata() {
        val db = writableDatabase
        db.delete(TABLE_NAME, null, null)
        db.close()
    }

    fun deletespec(value: String) {
        val db = writableDatabase
        db.delete(TABLE_NAME, "$COL_DATE='$value'", null)
        db.close()
    }

    fun updatedata(prev: String, new: String) {
        Log.i("i", "update")
        val db = readableDatabase
        val cv = ContentValues()
        cv.put(COL_VALUE, new)
        db.update(TABLE_NAME, cv, "$COL_VALUE='$prev'", null)
        db.close()
    }

    fun updatedata2(prev: String, new: String, date: String) {
        val db = readableDatabase
        val cv = ContentValues()
        val sum = prev.toInt() + new.toInt()
        val sumstr = sum.toString()
        cv.put(COL_VALUE, sumstr)
        db.update(TABLE_NAME, cv, "$COL_DATE='$date'", null)
        db.close()
    }
    fun insertmonth(monthdata:MonthData){
        val db=writableDatabase
        val cv=ContentValues()
        cv.put(COL_MONTH,monthdata.month)
        cv.put(COL_VALUE2,monthdata.monthvalue)
        db.insert(TABLE_NAME2,null,cv)
        db.close()
    }
    fun monthread():MutableList<MonthData>{
        val db=readableDatabase
        val res=db.rawQuery("SELECT * FROM $TABLE_NAME2",null)
        val list= mutableListOf<MonthData>()
        if(res.moveToFirst()){
            do{
                val m=MonthData()
                m.month=res.getString(res.getColumnIndex(COL_MONTH))
                m.monthvalue=res.getString(res.getColumnIndex(COL_VALUE2))
                list.add(m)
            }while (res.moveToNext())

        }
        res.close()
        db.close()
        return list
    }
    fun monthdel(){
        val db=writableDatabase
        db.delete(TABLE_NAME2,null,null)
        db.close()
    }
    fun monthupdate(prev: String, new: String, month: String) {
        val db = readableDatabase
        val cv = ContentValues()
        val sum = prev.toInt() + new.toInt()
        val sumstr = sum.toString()
        cv.put(COL_VALUE2, sumstr)
        db.update(TABLE_NAME2, cv, "$COL_MONTH='$month'", null)
        db.close()
    }
    fun monthdelspec(month:String){
        val db=writableDatabase
        db.delete(TABLE_NAME2,"$COL_MONTH= '$month'",null)
        db.close()
    }
}