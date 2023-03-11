package com.example.piggy_bank_assistant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import db.CategoryManager
import db.DbHelper

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        (findViewById(R.id.wipeAllButton) as Button).setOnClickListener {
            val categoryManager = CategoryManager(this)
            categoryManager.wipeData()
            Toast.makeText(applicationContext, R.string.data_wiped_message,
                Toast.LENGTH_SHORT).show()
        }
        (findViewById<Button>(R.id.back_btn)).setOnClickListener {
            finish()
        }
    }
}