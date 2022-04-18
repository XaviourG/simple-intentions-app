package com.xaviourg.simpleintentions

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import androidx.activity.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.xaviourg.simpleintentions.intentiondb.IntentionBlock
import com.xaviourg.simpleintentions.intentiondb.IntentionDatabase
import com.xaviourg.simpleintentions.intentiondb.IntentionRepository
import com.xaviourg.simpleintentions.intentiondb.IntentionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * Implementation of App Widget functionality.
 */
class MainWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    println("TRIGGERING HERE AT POINT 1")
    //create pending intent for opening app on click
    val pendingIntent = PendingIntent.getActivity(context, 0,
        Intent(context, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT)

    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.main_widget).apply {setOnClickPendingIntent(R.id.appwidget_text, pendingIntent)}

    //Grab Intentions from DB
    val db = IntentionDatabase.getDatabase(context)
    val dao = db.intentionDao()
    val list = dao.getAllIntentions().asLiveData()
    var observer = Observer<MutableList<IntentionBlock>> {
            l ->
        println("OBSERVED >>> ${l}")
        val i = l[0].intentions
        val text = "${i[0]} \n ${i[1]} \n ${i[2]}"
        println("Collated Intention as >>> $text")
        views.setTextViewText(R.id.appwidget_text, text)
        views.apply {setOnClickPendingIntent(R.id.appwidget_text, pendingIntent)}
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
    list.observeForever(observer)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}

fun setRemoteAdapter(context: Context, views: RemoteViews) {
    println("TRIGGERING HERE AT POINT 2")
    views.setRemoteAdapter(R.id.widget_list, Intent(context, WidgetService::class.java))
}
