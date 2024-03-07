package com.example.piggy_bank_assistant.presentation.category_history_activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.piggy_bank_assistant.R
import com.example.piggy_bank_assistant.databinding.ActivityCategoryBinding
import com.example.piggy_bank_assistant.domain.Category
import com.example.piggy_bank_assistant.domain.CategoryProportion
import com.example.piggy_bank_assistant.presentation.PiBAApplication
import com.example.piggy_bank_assistant.presentation.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CategoryHistoryActivity : AppCompatActivity() {

    private val binding by lazy { ActivityCategoryBinding.inflate(layoutInflater) }
    private val component by lazy { (application as PiBAApplication).component }
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[CategoryHistoryViewModel::class.java]
    }

    private lateinit var category: Category
    private val transactionAdapter = TransactionAdapter()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseIntent()
        getDependencies()
        setContent()
        bindButtonsClickListeners()
        setUpRecyclerView()
        observeViewModel()
    }

    private fun parseIntent() {
        if (!intent.hasExtra(CATEGORY_KEY)) {
            throw RuntimeException("Param category is absent")
        }
        category = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(
                CATEGORY_KEY,
                Category::class.java
            )
        } else {
            intent.getParcelableExtra<Category>(
                CATEGORY_KEY
            )
        }?:Category.getDefaultInstance()

    }

    private fun getDependencies() {
        component.inject(this)
    }

    private fun setContent() {
        val view = binding.root
        setContentView(view)
        binding.catNameCaption.text = category.name
    }

    private fun bindButtonsClickListeners() {
        binding.replenishBtn.setOnClickListener {
            addOperation(
                binding.replenishEnterBox.text.toString().toFloat(),
                1
            )
        }
        binding.subtractBtn.setOnClickListener {
            addOperation(
                binding.subtractEnterBox.text.toString().toFloat(),
                -1
            )
        }
        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    private fun setUpRecyclerView() {
        transactionAdapter.positiveTransactionColor = ContextCompat.getColor(
            applicationContext,
            R.color.green
        )
        binding.rvCategoryHistoryList.adapter = transactionAdapter
    }

    private fun observeViewModel() {
        lifecycleScope.launch(Dispatchers.IO) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getCategoryHistory(category).collect {
                    lifecycleScope.launch(Dispatchers.Main) {
                        transactionAdapter.submitList(it.transactions)
                    }
                }
            }
        }
        lifecycleScope.launch(Dispatchers.IO) {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.getCategoryById(category.id).collect {
                    withContext(Dispatchers.Main) {
                        binding.totalView.text = String.format("%.2f", it.amount)
                    }
                }
            }
        }
    }

    private fun addOperation(amount_:Float, sign:Int)
    {
        val amount = amount_*sign
        val categoriesProportion = listOf(CategoryProportion(
            category = category,
            proportion = 1f
        ))
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.addTransaction(categoriesProportion, amount)
        }
    }

    companion object{
        private const val CATEGORY_KEY = "category_kw"

        fun getIntent(context: Context, category: Category): Intent{
            val intent = Intent(context, CategoryHistoryActivity::class.java)
            intent.putExtra(CATEGORY_KEY, category)
            return intent
        }
    }
}