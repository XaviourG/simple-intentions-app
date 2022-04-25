package com.xaviourg.simpleintentions

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
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
import com.xaviourg.simpleintentions.intentiondb.Settings
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val intentionViewModel: IntentionViewModel by viewModels {
        IntentionViewModel.IntentionViewModelFactory((application as MyApplication).repository)
    }
    //settings stuff (set to defaults)
    private var weekStartsOn: DayOfWeek = DayOfWeek.MONDAY
    private var intentionCount: Int = 3
    private var mainScope: Scope = Scope.DAILY
    private var subLeftScope: Scope = Scope.MONTHLY
    private var subRightScope: Scope = Scope.YEARLY
    //adapters
    private lateinit var mainBlockAdapter: BlockAdapter
    private lateinit var subLeftBlockAdapter: BlockAdapter
    private lateinit var subRightBlockAdapter: BlockAdapter


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
        //window.setStatusBarColor(ContextCompat.getColor(this,R.color.white))

        //Get theme colours then colour the logo
        /*
        var tmp = TypedValue()
        theme.resolveAttribute(R.attr.colorPrimary, tmp, true)
        val primaryColour = tmp.data
        tmp = TypedValue()
        theme.resolveAttribute(R.attr.colorPrimaryVariant, tmp, true)
        val primaryColourVariant = tmp.data
        tmp = TypedValue()
        theme.resolveAttribute(R.attr.colorSecondary, tmp, true)
        val secondaryColour = tmp.data
        tmp = TypedValue()
        theme.resolveAttribute(R.attr.colorSecondaryVariant, tmp, true)
        val secondaryColourVariant = tmp.data
        val paint = binding.tvLogo.paint
        val width = paint.measureText(binding.tvLogo.text.toString())
        val textShader: Shader = LinearGradient(0f, 0f, width, binding.tvLogo.textSize, intArrayOf(
            primaryColour,
            primaryColourVariant,
            secondaryColour,
            secondaryColourVariant
        ), null, Shader.TileMode.REPEAT)
        binding.tvLogo.paint.setShader(textShader)
        */

        //Load Settings file and configure application
        intentionViewModel.settings.observe(this, {data ->
            data.let {
                println("Got Settings as $it")
                if(it.size <= 0){
                    intentionViewModel.insertSettings(Settings(
                        key = 1,
                        intentionCount = 3,
                        mainBlockScope = Scope.DAILY,
                        subLeftBlockScope = Scope.MONTHLY,
                        subRightBlockScope = Scope.YEARLY,
                        theme = "Light"
                    ))
                }else{
                    intentionCount = it[0].intentionCount
                    mainScope = it[0].mainBlockScope
                    subLeftScope = it[0].subLeftBlockScope
                    subRightScope = it[0].subRightBlockScope
                    //theme handling add here
                }

                //setup main intention block
                binding.tvMainBlockScope.setText("${mainScope.toString()} INTENTION")
                mainBlockAdapter = BlockAdapter(intentionViewModel, mainScope)
                binding.rvMainBlockContent.adapter = mainBlockAdapter
                binding.rvMainBlockContent.layoutManager = LinearLayoutManager(this)
                mainBlockAdapter.setListingsCount(intentionCount) //set # of intentions
                setLatestIntention(mainBlockAdapter, mainScope)//populate main intention block

                //setup sub left intention block
                binding.tvSubLeftBlockScope.setText("${subLeftScope.toString()} INTENTION")
                subLeftBlockAdapter = BlockAdapter(intentionViewModel, subLeftScope)
                binding.rvSubLeftBlockContent.adapter = subLeftBlockAdapter
                binding.rvSubLeftBlockContent.layoutManager = LinearLayoutManager(this)
                subLeftBlockAdapter.setListingsCount(intentionCount) //set # of intentions
                setLatestIntention(subLeftBlockAdapter, subLeftScope)//populate intention block

                //setup main intention block
                binding.tvSubRightBlockScope.setText("${subRightScope.toString()} INTENTION")
                subRightBlockAdapter = BlockAdapter(intentionViewModel, subRightScope)
                binding.rvSubRightBlockContent.adapter = subRightBlockAdapter
                binding.rvSubRightBlockContent.layoutManager = LinearLayoutManager(this)
                subRightBlockAdapter.setListingsCount(intentionCount) //set # of intentions
                setLatestIntention(subRightBlockAdapter, subRightScope)//populate intention block
            }
        })

        //====================== DB TESTING ===
        /*
        intentionViewModel.insertIntention(IntentionBlock(
            IBID = 1,
            date = LocalDate.parse("2013-04-07"),
            intentions = listOf("Awesome", "I", "AM"),
            scope = Scope.DECENNIAL
        ))
        intentionViewModel.insertIntention(IntentionBlock(
            IBID = 2,
            date = LocalDate.parse("2022-04-20"),
            intentions = listOf("Hi", "I", "AM"),
            scope = Scope.DAILY
        ))
        intentionViewModel.insertIntention(IntentionBlock(
            IBID = 3,
            date = LocalDate.parse("2022-04-17"),
            intentions = listOf("Bye", "I", "AM"),
            scope = Scope.YEARLY
        )) */
        //=====================================

        //Set settings button intent
        binding.btnSettings.setOnClickListener {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
    }

    override fun onPause() {
        super.onPause()
        mainBlockAdapter.save()
        subLeftBlockAdapter.save()
        subRightBlockAdapter.save()
        val awm = AppWidgetManager.getInstance(this)
        var rv = RemoteViews(this.packageName, R.layout.main_widget)
        //Update main widget if it exists
        val mainWidget = ComponentName(this, MainWidget::class.java)
        //rv.setTextViewText(R.id.widgetScope, "${mainScope.toString()} INTENTIONS")
        val intentionString = "« ${mainBlockAdapter.getIntentions().joinToString(" »\n« ")} »"
        rv.setTextViewText(R.id.appwidget_text, intentionString)
        awm.updateAppWidget(mainWidget, rv)
    }

    fun refresh() {
        finish()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setLatestIntention(adapter: BlockAdapter, scope: Scope) {
        println("Attempting to set intentions for scope::<$scope>")
        intentionViewModel.allIntentions.observe(this, {
                list -> list.let {
            //Get list of all intention blocks in scope
            val inScopeList = it.filter { it.scope == scope }
            if(inScopeList.isEmpty()){
                println("scope::<$scope> => scope is empty no intentions to load")
                //There exist no intention blocks do nothing for setup
            } else {
                //Take most recent block (by database query they should always be sorted by date)
                val mostRecent = inScopeList.first()
                //Scope handling to check the date is active
                println("scope::<$scope> => ${mostRecent.date.month} == ${LocalDate.now().month}")
                if (
                    ((scope == Scope.DAILY) and (mostRecent.date == LocalDate.now()))
                    or ((scope == Scope.WEEKLY) and (
                            ((LocalDate.now().toEpochDay() - mostRecent.date.toEpochDay()) <= 6)
                            ))
                    or ((scope == Scope.FORTNIGHTLY) and (
                            ((LocalDate.now().toEpochDay() - mostRecent.date.toEpochDay()) <= 13)
                            ))
                    or ((scope == Scope.MONTHLY) and (mostRecent.date.month == LocalDate.now().month))
                    or ((scope == Scope.QUARTERLY) and (
                            ((LocalDate.now().monthValue - mostRecent.date.monthValue) <= 2)
                            ))
                    or ((scope == Scope.BIYEARLY) and (
                            ((LocalDate.now().monthValue - mostRecent.date.monthValue) <= 5)
                            ))
                    or ((scope == Scope.YEARLY) and (mostRecent.date.year == LocalDate.now().year))
                    or ((scope == Scope.BIDECENNIAL) and (
                            ((LocalDate.now().year - mostRecent.date.year) <= 4)
                            ))
                    or ((scope == Scope.DECENNIAL) and (
                            ((LocalDate.now().year - mostRecent.date.year) <= 9)
                            ))
                    or ((scope == Scope.LIFE))
                ) {
                    adapter.setIntentions(mostRecent)
                } else {
                    println("scope::<$scope> => scope has intention blocks but not active ones")
                    //Un-active, no need to set anything
                }
            }
        }
        })
    }

    fun formatScope(s: String): String {
        var new = s.lowercase()
        new = new[0].uppercase() + new.subSequence(1, new.lastIndex + 1)
        return new
    }
}