package com.xaviourg.simpleintentions

import android.app.AlertDialog
import android.content.Intent
import android.graphics.LinearGradient
import android.graphics.Shader
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
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
        var tmp = TypedValue()
        theme.resolveAttribute(R.attr.colorOnBackground, tmp, true)
        window.setStatusBarColor(tmp.data)

        //Colourful Logo Bullshit
        tmp = TypedValue()
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

        //Set intent for back button
        binding.btnBack.setOnClickListener {
            //Check for illegal selection combinations...
            val scope1 = selectedToScope(binding.mainScopeSpinner.selectedItemPosition)
            val scope2 = selectedToScope(binding.subLeftScopeSpinner.selectedItemPosition)
            val scope3 = selectedToScope(binding.subRightScopeSpinner.selectedItemPosition)
            if((scope1 == scope2) or (scope1 == scope3) or (scope2 == scope3)){
                val alertBuilder = AlertDialog.Builder(this)
                alertBuilder.setTitle("Cannot Save These Settings")
                alertBuilder.setMessage("Intention Scopes must be unique!")
                alertBuilder.show()
            } else {

                //update settings
                db.insertSettings(
                    Settings(
                        key = 1,
                        intentionCount = getIntentionCount(),
                        mainBlockScope = scope1,
                        subLeftBlockScope = scope2,
                        subRightBlockScope = scope3,
                        theme = "Light"
                    )
                )
                //open main activity
                val mainIntent = Intent(this, MainActivity::class.java)
                startActivity(mainIntent)
            }
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
        //binding.themeSpinner.adapter = adapterScopeSpinner

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

    fun getIntentionCount(): Int {
        var value = -1
        when{
            binding.ic1.isChecked -> value = 1
            binding.ic2.isChecked -> value = 2
            binding.ic3.isChecked -> value = 3
            binding.ic4.isChecked -> value = 4
            binding.ic5.isChecked -> value = 5
        }
        return value
    }

    fun selectedToScope(value: Int): Scope {
        var scope = Scope.DAILY
        when(value) {
            0 -> scope = Scope.DAILY
            1 -> scope = Scope.WEEKLY
            2 -> scope = Scope.FORTNIGHTLY
            3 -> scope = Scope.MONTHLY
            4 -> scope = Scope.QUARTERLY
            5 -> scope = Scope.BIYEARLY
            6 -> scope = Scope.YEARLY
            7 -> scope = Scope.BIDECENNIAL
            8 -> scope = Scope.DECENNIAL
            9 -> scope = Scope.LIFE
        }
        return scope
    }
}