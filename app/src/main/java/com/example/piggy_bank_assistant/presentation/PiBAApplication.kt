package com.example.piggy_bank_assistant.presentation

import android.app.Application
import com.example.piggy_bank_assistant.di.DaggerApplicationComponent

class PiBAApplication : Application() {

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}
