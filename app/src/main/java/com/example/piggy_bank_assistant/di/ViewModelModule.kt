package com.example.piggy_bank_assistant.di

import androidx.lifecycle.ViewModel
import com.example.piggy_bank_assistant.presentation.category_add_activity.CategoryAddViewModel
import com.example.piggy_bank_assistant.presentation.category_history_activity.CategoryHistoryViewModel
import com.example.piggy_bank_assistant.presentation.main_activity.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CategoryAddViewModel::class)
    fun bindCategoryAddViewModel(viewModel: CategoryAddViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CategoryHistoryViewModel::class)
    fun bindCategoryHistoryViewModel(viewModel: CategoryHistoryViewModel): ViewModel
}
