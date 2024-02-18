package com.example.piggy_bank_assistant.presentation.category_add_activity

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.piggy_bank_assistant.R
import com.example.piggy_bank_assistant.databinding.ActivityAddCategoryBinding
import com.example.piggy_bank_assistant.domain.Category
import com.example.piggy_bank_assistant.presentation.PiBAApplication
import com.example.piggy_bank_assistant.presentation.ViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject


class CategoryAddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddCategoryBinding
    private lateinit var viewModel: CategoryAddViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (application as PiBAApplication).component
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        binding = ActivityAddCategoryBinding.inflate(layoutInflater)
        val view = binding.root
        viewModel = ViewModelProvider(this, viewModelFactory)[CategoryAddViewModel::class.java]
        setContentView(view)

        binding.addBtn.setOnClickListener {
            var checkedRadioButtonId = binding.radioGroup.checkedRadioButtonId
            if (checkedRadioButtonId == -1)
            {
                checkedRadioButtonId = binding.radioGroup2.checkedRadioButtonId
            }
            val radioButton: RadioButton = findViewById(checkedRadioButtonId)
            val color = radioButton.currentTextColor
            val newCategory = Category(
                id = 0,
                name = binding.nameEnterBox.text.toString(),
                amount = binding.amountEnterBox.text.toString().toFloat(),
                color = color
            )
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                    viewModel.addCategory(newCategory)
                }
            }
            finish()
        }
        binding.backBtn.setOnClickListener{
            finish()
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