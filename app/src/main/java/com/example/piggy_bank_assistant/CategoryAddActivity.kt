package com.example.piggy_bank_assistant

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import db.CategoryManager


class CategoryAddActivity : AppCompatActivity() {
    var manager = CategoryManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_category)

        val buttonAdd: Button = findViewById(R.id.add_btn)
        buttonAdd.setOnClickListener{
            val name: EditText = findViewById(R.id.name_enter_box)
            val amount: EditText = findViewById(R.id.amount_enter_box)
            var radioGroup: RadioGroup = findViewById(R.id.radioGroup)
            var checkedRadioButtonId = radioGroup.checkedRadioButtonId
            if (checkedRadioButtonId == -1)
            {
                radioGroup = findViewById(R.id.radioGroup2)
                checkedRadioButtonId = radioGroup.checkedRadioButtonId
            }
            val radioButton: RadioButton = findViewById(checkedRadioButtonId)
            var color = radioButton.currentTextColor
            manager.addCat(name.text.toString(), amount.text.toString().toInt(), color.toString())
            val intent = Intent(this@CategoryAddActivity, MainActivity::class.java)
            startActivity(intent)
        }
        val buttonCancell: Button = findViewById(R.id.back_btn)
        buttonCancell.setOnClickListener{
            val intent = Intent(this@CategoryAddActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
    fun group1Cheked(view: View){
        val radioGroup2: RadioGroup = findViewById(R.id.radioGroup2)
        if (radioGroup2.getCheckedRadioButtonId() != -1)
            radioGroup2.clearCheck()
    }
    fun group2Cheked(view: View){
        val radioGroup: RadioGroup = findViewById(R.id.radioGroup)
        if (radioGroup.getCheckedRadioButtonId() != -1)
            radioGroup.clearCheck()
    }
}