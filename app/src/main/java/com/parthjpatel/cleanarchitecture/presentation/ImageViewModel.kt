package com.parthjpatel.cleanarchitecture.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parthjpatel.cleanarchitecture.domain.model.DomainModel
import com.parthjpatel.cleanarchitecture.domain.useCases.GetImageUseCase
import kotlinx.coroutines.FlowPreview
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

@OptIn(FlowPreview::class)
class ImageViewModel : ViewModel() {

    private val useCase: GetImageUseCase by lazy { GetImageUseCase() }

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _query = MutableStateFlow("")

    init {
        viewModelScope.launch {
            _query.filter { it.isNotEmpty() }
                .distinctUntilChanged()
                .debounce(timeoutMillis = 1000)
                .collectLatest { query ->
                    getImages(query)
                }
        }
    }

    fun updateQuery(userInput: String) {
        _query.update { userInput }
    }

    fun getImages(q: String) {
        useCase(q).onStart { _uiState.update { UiState(isLoading = true) } }
            .onEach { result ->
                if (result.isSuccess) {
                    _uiState.update { UiState(data = result.getOrNull()) }
                } else {
                    _uiState.update { UiState(error = result.exceptionOrNull()?.message.toString()) }
                }
            }.catch { error ->
                _uiState.update { UiState(error = error.message.toString()) }
            }.launchIn(viewModelScope)
    }

}

data class UiState(
    val isLoading: Boolean = false,
    val error: String = "",
    val data: List<DomainModel>? = null,
)