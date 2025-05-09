package com.example.crmapp.viewmodels

import androidx.lifecycle.ViewModel
import com.example.crmapp.domain.usecase.interfaces.ContactUseCase
import com.example.crmapp.domain.usecase.interfaces.NoteUseCase

class ContactScreenViewModel(
    private val contactUseCase: ContactUseCase,
    private val noteUseCase: NoteUseCase
): ViewModel() {

}