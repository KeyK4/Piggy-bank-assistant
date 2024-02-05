package com.example.piggy_bank_assistant.presentation

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import com.example.piggy_bank_assistant.R
import com.example.piggy_bank_assistant.data.db.CategoryManager
import com.example.piggy_bank_assistant.data.db.DbHelper
import com.example.piggy_bank_assistant.data.db.HistoryManager

class CategoryHistoryActivity : AppCompatActivity() {
    var id = 0
    val historyManager = HistoryManager(this)
    val categoryManager = CategoryManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        id = intent.getIntExtra(DbHelper.ID_COL, 0)

        (findViewById(R.id.Hello_cat_View2) as TextView).setText(intent.getStringExtra(DbHelper.NAME_COl).toString())
        fillAmountText()
        fillScroll()
        (findViewById(R.id.replenish_btn) as Button).setOnClickListener {
            addOperation(
                (findViewById(R.id.replenish_enter_box) as TextView).text.toString().toFloat(),
                1
            )
            fillAmountText()
            fillScroll()
        }
        (findViewById(R.id.subtract_btn) as Button).setOnClickListener {
            addOperation(
                (findViewById(R.id.subtract_enter_box) as TextView).text.toString().toFloat(),
                -1
            )
            fillAmountText()
            fillScroll()
        }

        (findViewById(R.id.back_btn) as Button).setOnClickListener {
            val intent = Intent(this@CategoryHistoryActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun fillScroll()
    {
        val linearMaster: LinearLayout = findViewById(R.id.linearMaster)
        linearMaster.removeAllViews()
        val cursor = historyManager.getHistory(id)
        cursor.moveToLast()
        cursor.moveToNext()
        with(cursor!!)
        {
            while (this.moveToPrevious()) {
                val linearLayout = LinearLayout(this@CategoryHistoryActivity)
                linearLayout.orientation = LinearLayout.HORIZONTAL
                linearLayout.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                linearLayout.updateLayoutParams<LinearLayout.LayoutParams> {
                    setMargins(0, 0,0,30)
                }
                val amount = cursor.getString(cursor.getColumnIndexOrThrow(
                    com.example.piggy_bank_assistant.data.db.DbHelper.AMOUNT_COL)).toFloat()
                val date = cursor.getString(cursor.getColumnIndexOrThrow(
                    com.example.piggy_bank_assistant.data.db.DbHelper.DATE_COL))
                val amountText = createTextView(amount.toString(), Gravity.LEFT)
                if(amount > 0)
                    amountText.setTextColor(ContextCompat.getColor(applicationContext,
                        R.color.green
                    ))
                val dateText = createTextView(date, Gravity.RIGHT)
                linearLayout.addView(amountText)
                linearLayout.addView(dateText)
                linearMaster.addView(linearLayout)
            }
        }

    }

    private fun createTextView(text: String, gravity: Int): TextView
    {
        val textView = TextView(this)
        textView.setText(text)
        textView.gravity = gravity
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
        textView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        textView.updateLayoutParams<LinearLayout.LayoutParams> {
            weight = 1f
        }
        return textView
    }

    private fun addOperation(amount_:Float, sign:Int)
    {
        val amount = amount_*sign
        categoryManager.updateCatAmount(id, amount)
    }

    private fun fillAmountText()
    {
        val textView: TextView = findViewById(R.id.total_View)
        val cursor = categoryManager.getCatById(id)
        with(cursor)
        {
            while (this?.moveToNext()!!) {
                textView.setText(
                    cursor.getString(
                        cursor.getColumnIndexOrThrow(
                            DbHelper.AMOUNT_COL
                        )
                    )
                )
            }
        }
    }
}