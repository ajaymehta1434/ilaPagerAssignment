package com.neo.ilapagerassignment.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neo.ilapagerassignment.core.ApiState
import com.neo.ilapagerassignment.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject
constructor(private val mainRepository: MainRepository) : ViewModel() {

    private val gadgetsDataListFlow: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Empty)

    val _gadgetsDataListFlow: StateFlow<ApiState> = gadgetsDataListFlow

    fun getGadgetsList() = viewModelScope.launch {
        gadgetsDataListFlow.value = ApiState.Loading
        mainRepository.getMainData().catch { e ->
            gadgetsDataListFlow.value = ApiState.Failure(e)
        }.collect { data ->
            gadgetsDataListFlow.value = ApiState.Success(data)
        }
    }
}