package com.parthjpatel.cleanarchitecture.domain.model

sealed interface GetImageUiState {
    data object None : GetImageUiState
    data object Loading : GetImageUiState
    data class Success(val data: List<DomainModel>? = null) :
        GetImageUiState

    data class Error(val message: String) : GetImageUiState
}