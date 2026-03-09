package com.example.movieapp.ui.splashcreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashScreenViewModel : ViewModel() {
    // the first one is private and can be updated, meanwhile the second one is public and readonly
    private val _isSplashScreenVisible : MutableStateFlow<Boolean> = MutableStateFlow(true)

    // is used to access the value of the first one safely
    val isSplashScreenVisible : StateFlow<Boolean> = _isSplashScreenVisible.asStateFlow()
    init {

        // throws the SplashScreen and after, makes it invisible so it goes away
        viewModelScope.launch {
            _isSplashScreenVisible.value = false
        }
    }
}