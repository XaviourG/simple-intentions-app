package com.xaviourg.simpleintentions

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.RemoteViews
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.xaviourg.simpleintentions.databinding.ActivityMainBinding
import com.xaviourg.simpleintentions.intentiondb.IntentionBlock
import com.xaviourg.simpleintentions.intentiondb.IntentionViewModel
import com.xaviourg.simpleintentions.intentiondb.Scope
import java.time.LocalDateTime

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val intentionViewModel: IntentionViewModel by viewModels {
        IntentionViewModel.IntentionViewModelFactory((application as MyApplication).repository)
    }

    //@RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        //Establish view
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //repress top bar
        getSupportActionBar()!!.hide()
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black))

        //Load Settings file and configure application
            //Configure Card layout (num of intentions (1-5) and scopes displayed)
            //Configure Appearance


        //============================= TEST STUFF ========================================
        intentionViewModel.allIntentions.observe(this, {list ->
            list.let {
                for(entity in it){
                    println("FOUND ENTITY IN LIST <<$entity>>")
                }
            }
        })

        //setup main intention block
        val mainBlockAdapter = BlockAdapter(Scope.DAILY) //automate scope
        binding.rvMainBlockContent.adapter = mainBlockAdapter
        binding.rvMainBlockContent.layoutManager = LinearLayoutManager(this)
        mainBlockAdapter.setListingsCount(5) //automate count

        /*
        binding.btnTestSave.setOnClickListener {
            var todaysIntentions = mutableListOf<String>()
            todaysIntentions.add(binding.etTest1.text.toString())
            todaysIntentions.add(binding.etTest2.text.toString())
            todaysIntentions.add(binding.etTest3.text.toString())
            val new = IntentionBlock(IBID = 1, intentions = todaysIntentions, lastEdited = LocalDateTime.now().toString(), scope = Scope.DAILY)
            intentionViewModel.insertIntention(new)

            val awm = AppWidgetManager.getInstance(this)
            var rv = RemoteViews(this.packageName, R.layout.main_widget)
            val widget = ComponentName(this, MainWidget::class.java)
            rv.setTextViewText(R.id.appwidget_text, todaysIntentions.joinToString("\n"))
            awm.updateAppWidget(widget, rv)
        } */



    }
}