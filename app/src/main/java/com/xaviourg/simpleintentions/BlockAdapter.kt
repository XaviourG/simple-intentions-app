package com.xaviourg.simpleintentions

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.asFlow
import androidx.recyclerview.widget.RecyclerView
import com.xaviourg.simpleintentions.databinding.IntentionListingBinding
import com.xaviourg.simpleintentions.intentiondb.IntentionBlock
import com.xaviourg.simpleintentions.intentiondb.IntentionViewModel
import com.xaviourg.simpleintentions.intentiondb.Scope
import kotlinx.coroutines.flow.toList
import java.time.DayOfWeek
import java.time.LocalDate

class BlockAdapter(private val db: IntentionViewModel, private val scope: Scope) : RecyclerView.Adapter<BlockAdapter.BlockViewHolder>() {
    private var intentions = mutableListOf<String>()
    private var IBID: Int = -1
    private var date: LocalDate? = null


    class BlockViewHolder(val binding: IntentionListingBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlockViewHolder {
        val binding = IntentionListingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BlockViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BlockViewHolder, position: Int) {
        //populate intention listings
        holder.binding.textView.setText(intentions[position])
        //save any changes to listing to parent
        holder.binding.textView.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                intentions[holder.adapterPosition] = holder.binding.textView.text.toString()
            }

        })
    }

    override fun getItemCount(): Int {
        return intentions.size
    }

    fun setListingsCount(count: Int){
        var newList = mutableListOf<String>()
        for (i in 1..count) {
            newList.add("")
        }
        intentions = newList
        notifyDataSetChanged()
    }

    fun setIntentions(intentionBlock: IntentionBlock) {
        IBID = intentionBlock.IBID!!
        date = intentionBlock.date
        if (intentions.size == intentionBlock.intentions.size) {
            intentions = intentionBlock.intentions.toMutableList()
            println("Setting intentions of scope::<${scope}> to $intentions")
        } else {
            println("Intentions size does not match intentionBlock intentions list !!!")
            var newList = mutableListOf<String>()
            var i = 0
            while(i < intentions.size) { //first get a list of size intentions
                newList.add("")
                i++
            }
            i = 0 //then fill with intentions
            while((i < intentions.size) and (i < intentionBlock.intentions.size)) {
                newList[i] = intentionBlock.intentions[i]
                i++
            }
            intentions = newList
        }
        notifyDataSetChanged()
    }

    fun save() { //save intention block to database
        var newIntentionBlock: IntentionBlock? = null
        if (IBID == -1) { //not loaded from intention, new
            val propperDate = getDate(scope)
            date = propperDate
            newIntentionBlock = IntentionBlock(
                intentions = intentions,
                scope = scope,
                date = date!!
            )
            //update IBID
            //somehow reset whol view so it loads back with an IBID
        } else {
            newIntentionBlock = IntentionBlock(
                IBID = IBID,
                intentions = intentions,
                scope = scope,
                date = date!!
            )
        }
        db.insertIntention(newIntentionBlock)
    }

    fun getDate(scope: Scope): LocalDate {
        //return the start date of this intention block given scope...
        // yearly intentions start at start of year regardless of actual start, etc...
        var day = LocalDate.now()
        when(scope) {
            Scope.WEEKLY -> day = thisWeekStart()
            Scope.FORTNIGHTLY -> day = thisWeekStart()
            Scope.MONTHLY -> day = thisMonthStart()
            Scope.QUARTERLY -> day = thisQuarterStart()
            Scope.BIYEARLY-> day = thisHalfYearStart()
            Scope.YEARLY -> day = thisYearStart()
            Scope.BIDECENNIAL -> day = thisYearStart()
            Scope.DECENNIAL -> day = thisYearStart()
        }
        return day
    }

    fun thisWeekStart(): LocalDate {
        var i = 0
        val weekStart = DayOfWeek.MONDAY
        var day = LocalDate.now()
        while (day.dayOfWeek != weekStart) {
            i++
            day = day.minusDays(1)
        }
        return day
    }

    fun thisMonthStart(): LocalDate {
        return LocalDate.now().minusDays((LocalDate.now().dayOfMonth - 1).toLong())
    }

    fun thisQuarterStart(): LocalDate {
        var day = thisMonthStart()
        if(day.monthValue >= 10) {
            day = day.minusMonths((day.monthValue - 10).toLong())
        } else if(day.monthValue >= 7) {
            day = day.minusMonths((day.monthValue - 7).toLong())
        } else if (day.monthValue >= 4) {
            day = day.minusMonths((day.monthValue - 4).toLong())
        } else {
            day = day.minusMonths((day.monthValue - 1).toLong())
        }
        return day
    }

    fun thisHalfYearStart(): LocalDate {
        var day = thisMonthStart()
        if(day.monthValue >= 7) {
            day = day.minusMonths((day.monthValue - 7).toLong())
        } else {
            day = day.minusMonths((day.monthValue - 1).toLong())
        }
        return day
    }

    fun thisYearStart(): LocalDate {
        return LocalDate.now().minusDays((LocalDate.now().dayOfYear - 1).toLong())
    }

    fun getIntentions(): List<String> {
        return intentions
    }
}