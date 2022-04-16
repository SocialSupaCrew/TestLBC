package com.testlbc.core

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        return super.onCreateView(parent, name, context, attrs)
    }
}
