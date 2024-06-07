package com.cookiehunterrr.profilelookup.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {
    private var _inputText = MutableLiveData<String>()
    val inputText : LiveData<String> = _inputText

    fun updateTextInInput(text: String)
    {
        _inputText.value = text
    }
}