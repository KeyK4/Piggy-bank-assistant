package com.example.piggy_bank_assistant.di

import android.app.Application
import com.example.piggy_bank_assistant.presentation.category_add_activity.CategoryAddActivity
import com.example.piggy_bank_assistant.presentation.main_activity.MainActivity
import com.example.piggy_bank_assistant.presentation.ViewModelFactory
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        DataModule::class,
        ViewModelModule::class
    ]
)
interface ApplicationComponent {

    fun inject(activity: MainActivity)

    fun inject(fragment: CategoryAddActivity)

    fun viewModelsFactory(): ViewModelFactory

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}
