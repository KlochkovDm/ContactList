package com.example.contactlist.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.contactlist.AppState
import com.example.contactlist.repository.RepositoryContactImpl
import com.example.contactlist.repository.RepositoryContact

class MainViewModel(private val repository : RepositoryContact = RepositoryContactImpl()) : ViewModel() {
    val contacts: MutableLiveData<AppState> = MutableLiveData()

    fun getContacts() {
        contacts.value = AppState.Loading
        val answer = repository.getListOfContact()
        contacts.value = AppState.Success(answer)
    }
}