package com.parthjpatel.cleanarchitecture.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parthjpatel.cleanarchitecture.domain.useCases.GetImageUseCase
import com.parthjpatel.cleanarchitecture.presentation.searchUIState.GetImageUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor(private val useCase: GetImageUseCase) : ViewModel() {

//    private val useCase: GetImageUseCase by lazy { GetImageUseCase() }

    private val _uiState =
        MutableStateFlow<GetImageUiState>(GetImageUiState.Success(data = emptyList()))
    val uiState = _uiState.asStateFlow()

    private val _query = MutableStateFlow("")

    init {
        viewModelScope.launch {
            _query.filter { it.isNotEmpty() }
                .distinctUntilChanged()
                .debounce(timeoutMillis = 500)
                .collectLatest { query ->
                    getImages(query)
                }
        }
    }

    fun updateQuery(userInput: String) {
        _query.update { userInput }
    }

    fun getImages(q: String) {
        useCase(q).onStart { _uiState.update { GetImageUiState.Loading } }
            .onEach { result ->
                if (result.isSuccess) {
                    _uiState.update { GetImageUiState.Success(data = result.getOrDefault(emptyList())) }
                } else {
                    _uiState.update { GetImageUiState.Error(message = result.exceptionOrNull()?.message.toString()) }
                }
            }.catch { error ->
                _uiState.update { GetImageUiState.Error(message = error.message.toString()) }
            }.launchIn(viewModelScope)
    }

}
