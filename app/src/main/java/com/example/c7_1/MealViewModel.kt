package com.example.c7_1

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MealViewModel : ViewModel(){

    private val _mealState = MutableStateFlow<MealState>(MealState.Initial)
    val mealState: StateFlow<MealState> = _mealState

    fun fetchMeal(foodName: String) {
        viewModelScope.launch {
            _mealState.value = MealState.Loading
            try {
                val mealResponse = ApiClient.apiSerive.getMeal(foodName)
                _mealState.value = MealState.Success(mealResponse)
            } catch (e: Exception) {
                _mealState.value = MealState.Error(e.message ?: "Unknown error")
            }
        }
    }

    sealed class MealState {
        object Initial : MealState()
        object Loading : MealState()
        data class Success(val mealResponse: MealResponse) : MealState()
        data class Error(val errorMessage: String) : MealState()
    }

}