package com.parthjpatel.cleanarchitecture.presentation.useCases

import com.parthjpatel.cleanarchitecture.data.repository.ImagesRepositoryImpl
import com.parthjpatel.cleanarchitecture.domain.model.DomainModel
import com.parthjpatel.cleanarchitecture.domain.repository.ImagesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GetImageUseCase {

    private val imagesRepository: ImagesRepository by lazy { ImagesRepositoryImpl() }

    operator fun invoke(q: String) = flow<Result<List<DomainModel>>> {
        val response = imagesRepository.getImages(q)
        emit(response)
    }.catch {
        emit(Result.failure(it))
    }.flowOn(Dispatchers.IO)
}