package com.karan.readsms

import android.app.ListActivity
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.TextView
import java.util.*
import android.Manifest
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat

class MainActivity : ListActivity() {
    val SMS = Uri.parse("content://sms")
    val PERMISSIONS_REQUEST_READ_SMS = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        readSMS()
        val permissionCheck = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_CALENDAR)
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
//            != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, Array<String>(1) { Manifest.permission.READ_SMS}, 1)
//        }
//        else {
//        }
        if(permissionCheck == PackageManager.PERMISSION_GRANTED) {
            readSMS()
        }
        else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_SMS),PERMISSIONS_REQUEST_READ_SMS)
        }

    }
    object SmsColumns {
        val ID = "_id"
        val ADDRESS = "address"
        val DATE = "date"
        val BODY = "body"
    }
    private inner class SmsCursorAdapter(context: Context, c: Cursor, autoRequery:Boolean):CursorAdapter(context,c,autoRequery) {
        override fun newView(context: Context?, cursor: Cursor?, viewGroup: ViewGroup?): View {
            return View.inflate(context,R.layout.activity_main,null)
        }

        override fun bindView(view: View?, context: Context?, cursor: Cursor?) {
            view!!.findViewById<TextView>(R.id.sms_origin).text = cursor!!.getString(cursor.getColumnIndexOrThrow(SmsColumns.ADDRESS))
            view.findViewById<TextView>(R.id.sms_body).text = cursor!!.getString(cursor.getColumnIndexOrThrow(SmsColumns.DATE)).toString()
            view.findViewById<TextView>(R.id.sms_date).text = Date(cursor.getLong(cursor.getColumnIndexOrThrow(SmsColumns.DATE))).toString()
        }
    }

    private fun readSMS() {
        val cursor = contentResolver.query(SMS, arrayOf(SmsColumns.ID,SmsColumns.ADDRESS,SmsColumns.DATE,SmsColumns.BODY),null,null,SmsColumns.DATE + "DESC")
        val adapter = SmsCursorAdapter(this,cursor,true)
        listAdapter = adapter
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            PERMISSIONS_REQUEST_READ_SMS -> {
                if(grantResults?.isNotEmpty()!! && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    readSMS()
                }
                else {

                }
                return
            }
        }
    }

}
