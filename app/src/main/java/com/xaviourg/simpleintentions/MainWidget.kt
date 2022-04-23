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
import com.xaviourg.simpleintentions.intentiondb.*
import kotlinx.coroutines.*
import java.time.LocalDate

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
    //val pendingIntent = PendingIntent.getActivity(context, 0,
    //    Intent(context, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT)

    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.main_widget)

    //Grab Intentions from DB
    val db = IntentionDatabase.getDatabase(context)
    val dao = db.intentionDao()
    val settings = dao.settings().asLiveData()
    var scope = Scope.LIFE
    val list = dao.getAllIntentions().asLiveData()
    var observer = Observer<MutableList<IntentionBlock>> {
            l ->
        if(l.size > 0) {
            for (ib in l) {
                //since it orders by descending date, first instance found will be most recent
                if (activeIntention(ib, scope)){
                    val text = ib.intentions.joinToString("\n")
                    views.setTextViewText(R.id.appwidget_text, text)
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                    break
                }
            }
        }
    }
    var settingsObserver = Observer<MutableList<Settings>> {
                s ->
            println("Main widget observes settings file::$settings")
            if (s.size <= 0) {} else {
            scope = s[0].mainBlockScope
            println("Main Widget got scope::$scope, checking for active intention")
            list.observeForever(observer)
            breakObserver(list, observer)
        }
    }
    settings.observeForever(settingsObserver)
    breakSettingsObserver(settings, settingsObserver)
}

fun breakObserver(list: LiveData<MutableList<IntentionBlock>>, observer: Observer<MutableList<IntentionBlock>>) = runBlocking {
    launch {
        delay(1000)
        list.removeObserver(observer)
    }
}

fun breakSettingsObserver(list: LiveData<MutableList<Settings>>, observer: Observer<MutableList<Settings>>) = runBlocking {
    launch {
        delay(3000)
        list.removeObserver(observer)
    }
}

fun activeIntention(ib: IntentionBlock, scope: Scope): Boolean {
    var res = false
    if(
            ((scope == Scope.DAILY) and (ib.date == LocalDate.now()))
                    or ((scope == Scope.WEEKLY) and (
                    ((LocalDate.now().toEpochDay() - ib.date.toEpochDay()) <= 6)
                    ))
                    or ((scope == Scope.FORTNIGHTLY) and (
                    ((LocalDate.now().toEpochDay() - ib.date.toEpochDay()) <= 13)
                    ))
                    or ((scope == Scope.MONTHLY) and (ib.date.month == LocalDate.now().month))
                    or ((scope == Scope.QUARTERLY) and (
                    ((LocalDate.now().monthValue - ib.date.monthValue) <= 2)
                    ))
                    or ((scope == Scope.BIYEARLY) and (
                    ((LocalDate.now().monthValue - ib.date.monthValue) <= 5)
                    ))
                    or ((scope == Scope.YEARLY) and (ib.date.year == LocalDate.now().year))
                    or ((scope == Scope.BIDECENNIAL) and (
                    ((LocalDate.now().year - ib.date.year) <= 4)
                    ))
                    or ((scope == Scope.DECENNIAL) and (
                    ((LocalDate.now().year - ib.date.year) <= 9)
                    ))
                    or ((scope == Scope.LIFE))
            ) {
        res = true
    }
    return res
}
