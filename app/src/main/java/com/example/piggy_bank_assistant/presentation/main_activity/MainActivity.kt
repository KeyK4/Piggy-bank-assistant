package com.example.piggy_bank_assistant.presentation.main_activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.piggy_bank_assistant.data.db.DbHelper
import com.example.piggy_bank_assistant.databinding.ActivityMainBinding
import com.example.piggy_bank_assistant.presentation.CategoryHistoryActivity
import com.example.piggy_bank_assistant.presentation.ConsumptionAdd
import com.example.piggy_bank_assistant.presentation.IncomeAddActivity
import com.example.piggy_bank_assistant.presentation.PiBAApplication
import com.example.piggy_bank_assistant.presentation.SettingsActivity
import com.example.piggy_bank_assistant.presentation.ViewModelFactory
import com.example.piggy_bank_assistant.presentation.category_add_activity.CategoryAddActivity
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainActivity : AppCompatActivity() {
    @SuppressLint("ResourceAsColor")

    private lateinit var binding: ActivityMainBinding
    private lateinit var categoriesListAdapter: MainCategoryAdapter
    private lateinit var viewModel: MainViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (application as PiBAApplication).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        setUpButtons()
        setUpCategoriesListAdapter()
        binding.rvCategoryList.adapter = categoriesListAdapter
        setupSwipeListener(binding.rvCategoryList)
        observeViewModel()
    }

    private fun setUpButtons() {
        binding.incomeBtn.setOnClickListener {
            val intent = Intent(this@MainActivity, IncomeAddActivity::class.java)
            startActivity(intent)
        }
        binding.consumptionBtn.setOnClickListener {
            val intent = Intent(this@MainActivity, ConsumptionAdd::class.java)
            startActivity(intent)
        }
        binding.imageView2.setOnClickListener {
            val intent = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(intent)
        }
        binding.categoryAddButton.setOnClickListener {
            val intent = Intent(this@MainActivity, CategoryAddActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setUpCategoriesListAdapter() {
        categoriesListAdapter = MainCategoryAdapter()
        with(categoriesListAdapter){
            onCategoryClickListener = {
                val intent = Intent(this@MainActivity, CategoryHistoryActivity::class.java)
                intent.putExtra(DbHelper.ID_COL, it.id)
                intent.putExtra(DbHelper.NAME_COl, it.name)
                startActivity(intent)
            }
        }

    }

    private fun observeViewModel(){
        lifecycleScope.launch {
            viewModel.getAllCategories().collect{
                categoriesListAdapter.submitList(it)
            }
        }
    }

    private fun setupSwipeListener(rvShopList: RecyclerView) {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT
        ) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = categoriesListAdapter.currentList[
                        viewHolder.adapterPosition]
                lifecycleScope.launch {
                    viewModel.deleteCategory(item)
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvShopList)
    }
}