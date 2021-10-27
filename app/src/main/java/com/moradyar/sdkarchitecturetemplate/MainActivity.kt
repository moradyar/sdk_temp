package com.moradyar.sdkarchitecturetemplate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.moradyar.gridfeedview.GridFeedView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<GridFeedView>(R.id.gfc).callMe()
    }
}