package com.example.tp

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.view.MenuItem
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.wear.widget.drawer.WearableActionDrawerView
import androidx.wear.widget.drawer.WearableNavigationDrawerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : WearableActivity(), MenuItem.OnMenuItemClickListener {

    private var notificationId = 1
    private val id = "my_channel_01"

    lateinit var wearableNavigationDrawer : WearableNavigationDrawerView
    lateinit var wearableActionDrawer: WearableActionDrawerView
    lateinit var  options: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Enables Always-on
        setAmbientEnabled()

        val eventId = 1

        createNotificationChannel()

        val viewPendingIntent = Intent(this, ViewEventActivity::class.java).let {
            it.putExtra(EXTRA_EVENT_ID, eventId)
            PendingIntent.getActivity(this, REQUEST_CODE, it, 0)
        }

        val notificationBuilder = NotificationCompat.Builder(this, id)
            .setSmallIcon(android.R.drawable.ic_dialog_email)
            .setContentTitle("Notificación")
            .setContentIntent(viewPendingIntent)
            .setStyle(NotificationCompat.BigTextStyle().bigText(getString(R.string.large_text)))
        //.addAction(android.R.drawable.ic_menu_add, getString(R.string.app_name), viewPendingIntent)

        ibNotificationCheck.setOnClickListener {
            NotificationManagerCompat.from(this).apply {
                notify(notificationId, notificationBuilder.build())
            }
            //notificationId++
        }

        //Notifications
        loadOptions()
        wearableNavigationDrawer = findViewById(R.id.wnTop)
        wearableNavigationDrawer.setAdapter(NavigationAdapter(this))

        wearableNavigationDrawer.controller.peekDrawer()


    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Wear OS Notification Channel"
            val description = "Notification Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(id, name, importance)
            channel.description = description

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val EXTRA_EVENT_ID = "extra_event_id"
        const val REQUEST_CODE = 1
    }

    private fun loadOptions() {
        options = ArrayList()
        options.add("Home")
        options.add("Favorite")
        options.add("Account")
    }

    override fun onMenuItemClick(p0: MenuItem?): Boolean {
        return true
    }

    inner class NavigationAdapter(context: Context) : WearableNavigationDrawerView.WearableNavigationDrawerAdapter() {
        override fun getItemText(pos: Int): CharSequence {
            return options[pos]
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        override fun getItemDrawable(pos: Int): Drawable {
            return resources.getDrawable(R.drawable.card_background, null)
        }

        override fun getCount(): Int {
            return options.size
        }
    }
}
