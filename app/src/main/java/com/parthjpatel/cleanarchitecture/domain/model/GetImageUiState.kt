package com.parthjpatel.cleanarchitecture.domain.model

sealed interface GetImageUiState {
    data object Loading : GetImageUiState
    data class Success(val data: List<DomainModel>) : GetImageUiState
    data class Error(val message: String) : GetImageUiState
}