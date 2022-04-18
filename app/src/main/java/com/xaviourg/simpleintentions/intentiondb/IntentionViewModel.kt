package com.xaviourg.simpleintentions.intentiondb

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class IntentionViewModel(private val intentionRepository: IntentionRepository) : ViewModel() {
    val allIntentions : LiveData<MutableList<IntentionBlock>> = intentionRepository.allIntentions.asLiveData()


    // Launching a new coroutine to insert the data in a non-blocking way

    fun insertIntention(intentionBlock: IntentionBlock) = viewModelScope.launch {
        intentionRepository.insertIntention(intentionBlock)
    }

    fun deleteIntention(intentionBlock: IntentionBlock) = viewModelScope.launch {
        intentionRepository.deleteIntention(intentionBlock)
    }

    fun updateIntention(intentionBlock: IntentionBlock) = viewModelScope.launch {
        intentionRepository.updateIntention(intentionBlock)
    }

    //Singleton initialisation of view model
    class IntentionViewModelFactory(private val intentionRepository: IntentionRepository)
        : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(IntentionViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return IntentionViewModel(intentionRepository) as T
            }
            throw IllegalArgumentException("Unknown VieModel Class")
        }

    }
}