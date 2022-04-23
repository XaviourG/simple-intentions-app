package com.xaviourg.simpleintentions

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.xaviourg.simpleintentions.databinding.ActivityMainBinding
import com.xaviourg.simpleintentions.databinding.ActivitySettingsBinding
import com.xaviourg.simpleintentions.intentiondb.IntentionViewModel
import com.xaviourg.simpleintentions.intentiondb.Scope
import com.xaviourg.simpleintentions.intentiondb.Settings

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private val db: IntentionViewModel by viewModels {
        IntentionViewModel.IntentionViewModelFactory((application as MyApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //Establish view
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //repress top bar
        getSupportActionBar()!!.hide()
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.white))

        //Set intent for back button
        binding.btnBack.setOnClickListener {
            //update settings

            //open main activity
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
        }

        //Set Scope Spinners
        val scopes = resources.getStringArray(R.array.Scopes)
        val adapterMainSpinner = ArrayAdapter(this, android.R.layout.simple_spinner_item, scopes)
        binding.mainScopeSpinner.adapter = adapterMainSpinner
        val adapterSLSpinner = ArrayAdapter(this, android.R.layout.simple_spinner_item, scopes)
        binding.subLeftScopeSpinner.adapter = adapterSLSpinner
        val adapterSRSpinner = ArrayAdapter(this, android.R.layout.simple_spinner_item, scopes)
        binding.subRightScopeSpinner.adapter = adapterSRSpinner

        //Set Theme Spinner
        val themes = resources.getStringArray(R.array.Themes)
        val adapterScopeSpinner = ArrayAdapter(this, android.R.layout.simple_spinner_item, themes)
        binding.themeSpinner.adapter = adapterScopeSpinner

        //Load in existing settings & prepopulate options
        db.settings.observe(this, {data ->
            data.let {
                println("Got Settings as $it")
                //Set intention count radial
                when(it[0].intentionCount){
                    1 -> binding.ic1.isChecked = true
                    2 -> binding.ic2.isChecked = true
                    3 -> binding.ic3.isChecked = true
                    4 -> binding.ic4.isChecked = true
                    5 -> binding.ic5.isChecked = true
                }
                //set scopes
                when(it[0].mainBlockScope){
                    Scope.DAILY -> binding.mainScopeSpinner.setSelection(0)
                    Scope.WEEKLY -> binding.mainScopeSpinner.setSelection(1)
                    Scope.FORTNIGHTLY -> binding.mainScopeSpinner.setSelection(2)
                    Scope.MONTHLY -> binding.mainScopeSpinner.setSelection(3)
                    Scope.QUARTERLY -> binding.mainScopeSpinner.setSelection(4)
                    Scope.BIYEARLY -> binding.mainScopeSpinner.setSelection(5)
                    Scope.YEARLY -> binding.mainScopeSpinner.setSelection(6)
                    Scope.BIDECENNIAL -> binding.mainScopeSpinner.setSelection(7)
                    Scope.DECENNIAL -> binding.mainScopeSpinner.setSelection(8)
                    Scope.LIFE -> binding.mainScopeSpinner.setSelection(9)
                }
                when(it[0].subLeftBlockScope){
                    Scope.DAILY -> binding.subLeftScopeSpinner.setSelection(0)
                    Scope.WEEKLY -> binding.subLeftScopeSpinner.setSelection(1)
                    Scope.FORTNIGHTLY -> binding.subLeftScopeSpinner.setSelection(2)
                    Scope.MONTHLY -> binding.subLeftScopeSpinner.setSelection(3)
                    Scope.QUARTERLY -> binding.subLeftScopeSpinner.setSelection(4)
                    Scope.BIYEARLY -> binding.subLeftScopeSpinner.setSelection(5)
                    Scope.YEARLY -> binding.subLeftScopeSpinner.setSelection(6)
                    Scope.BIDECENNIAL -> binding.subLeftScopeSpinner.setSelection(7)
                    Scope.DECENNIAL -> binding.subLeftScopeSpinner.setSelection(8)
                    Scope.LIFE -> binding.subLeftScopeSpinner.setSelection(9)
                }
                when(it[0].subRightBlockScope){
                    Scope.DAILY -> binding.subRightScopeSpinner.setSelection(0)
                    Scope.WEEKLY -> binding.subRightScopeSpinner.setSelection(1)
                    Scope.FORTNIGHTLY -> binding.subRightScopeSpinner.setSelection(2)
                    Scope.MONTHLY -> binding.subRightScopeSpinner.setSelection(3)
                    Scope.QUARTERLY -> binding.subRightScopeSpinner.setSelection(4)
                    Scope.BIYEARLY -> binding.subRightScopeSpinner.setSelection(5)
                    Scope.YEARLY -> binding.subRightScopeSpinner.setSelection(6)
                    Scope.BIDECENNIAL -> binding.subRightScopeSpinner.setSelection(7)
                    Scope.DECENNIAL -> binding.subRightScopeSpinner.setSelection(8)
                    Scope.LIFE -> binding.subRightScopeSpinner.setSelection(9)
                }
                //set theme

            }
        })
    }
}