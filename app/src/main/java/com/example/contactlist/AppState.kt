package com.example.contactlist

sealed class AppState {
    class Success(val data: List<String>) : AppState()
    object Loading : AppState()
}