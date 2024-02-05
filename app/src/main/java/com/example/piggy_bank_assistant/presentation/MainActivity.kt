package com.example.piggy_bank_assistant.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import com.example.piggy_bank_assistant.R
import com.example.piggy_bank_assistant.data.db.CategoryManager
import com.example.piggy_bank_assistant.data.db.DbHelper


class MainActivity : AppCompatActivity() {
    @SuppressLint("ResourceAsColor")

    val manager = CategoryManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (findViewById(R.id.income_btn) as Button).setOnClickListener{
            val intent = Intent(this@MainActivity, IncomeAddActivity::class.java)
            startActivity(intent)
        }
        (findViewById(R.id.consumption_btn) as Button).setOnClickListener {
            val intent = Intent(this@MainActivity, ConsumptionAdd::class.java)
            startActivity(intent)
        }
        (findViewById(R.id.imageView2)as ImageView).setOnClickListener{
            val intent = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        Fill_home_container()
    }

    fun Fill_home_container()
    {
        val linearLayout: LinearLayout = findViewById(R.id.home_container)
        linearLayout.removeAllViews()
        val button = Button(this)
        val res: Resources = baseContext.getResources()
        button.setText(res.getString(R.string.category_add_button_str))
        button.gravity = Gravity.LEFT
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18F)
        button.setBackgroundResource(R.drawable.button_main)
        button.background.setTint(Color.parseColor("#FFCCCCCC"))
        button.setOnClickListener {
            val intent = Intent(this@MainActivity, CategoryAddActivity::class.java)
            startActivity(intent)
        }
        linearLayout.addView(button)
        val cursor = manager.getCats()
        var sum = 0f
        with(cursor!!)
        {
            while (this.moveToNext()) {
                val textView1 = createTextView(cursor.getString(
                    cursor.getColumnIndexOrThrow(DbHelper.NAME_COl)),
                    Gravity.LEFT,
                    1f
                )
                val textView2 = createTextView(cursor.getString(
                    cursor.getColumnIndexOrThrow(DbHelper.AMOUNT_COL)),
                    Gravity.RIGHT,
                     3f
                )
                sum += textView2.text.toString().toFloat()
                val layout = LinearLayout(this@MainActivity)
                layout.setBackgroundResource(R.drawable.button_main)
                layout.background.setTint(cursor.getString(
                    cursor.getColumnIndexOrThrow(DbHelper.COLOR_COL)).toInt())
                layout.addView(textView1)
                layout.addView(textView2)
                layout.id = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.ID_COL)).toInt()
                layout.setOnClickListener {
                    val intent = Intent(this@MainActivity, CategoryHistoryActivity::class.java)
                    intent.putExtra(DbHelper.ID_COL, it.id)
                    val Viewlayout = it as LinearLayout
                    intent.putExtra(DbHelper.NAME_COl, (Viewlayout.getChildAt(0) as TextView).text)
                    startActivity(intent)
                }

                linearLayout.addView(layout)
            }
        }
        (findViewById(R.id.textView6) as TextView).setText(sum.toString())
    }

    fun createTextView(text:String, gravity: Int, weight_:Float):TextView
    {
        val textView = TextView(this@MainActivity)
        textView.setText(text)
        textView.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
        textView.gravity = gravity
        textView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18F)
        textView.maxLines = 1
        textView.ellipsize = TextUtils.TruncateAt.END
        textView.isSingleLine = true
        textView.updateLayoutParams<LinearLayout.LayoutParams> {
            weight = weight_
        }
        return textView
    }
}