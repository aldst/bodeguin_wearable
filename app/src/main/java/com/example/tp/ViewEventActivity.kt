package com.example.tp


import android.os.Bundle
import android.support.wearable.activity.WearableActivity

class ViewEventActivity : WearableActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_event)
    }
}