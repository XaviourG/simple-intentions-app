package com.xaviourg.simpleintentions

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.xaviourg.simpleintentions.intentiondb.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.toList
import java.util.concurrent.Flow

class WidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return WidgetRemoteViewFactory(this.applicationContext, intent)
    }
}

class WidgetRemoteViewFactory(private val context: Context, intent: Intent) : RemoteViewsService.RemoteViewsFactory {
    private lateinit var list: LiveData<MutableList<IntentionBlock>>
    private lateinit var dao: IntentionDao

    override fun onCreate() {
        println("Triggering at point 4!!!!")
        val db = IntentionDatabase.getDatabase(context)
        dao = db.intentionDao()
        list = dao.getAllIntentions().asLiveData()
        println("HERES THE LIST:: $list")
    }

    override fun onDataSetChanged() {
        list = dao.getAllIntentions().asLiveData()
    }

    override fun onDestroy() {
        TODO("Not yet implemented")
    }

    override fun getCount(): Int {
        TODO("Not yet implemented")
    }

    override fun getViewAt(p0: Int): RemoteViews {
        TODO("Not yet implemented")
    }

    override fun getLoadingView(): RemoteViews {
        TODO("Not yet implemented")
    }

    override fun getViewTypeCount(): Int {
        TODO("Not yet implemented")
    }

    override fun getItemId(p0: Int): Long {
        TODO("Not yet implemented")
    }

    override fun hasStableIds(): Boolean {
        TODO("Not yet implemented")
    }

}