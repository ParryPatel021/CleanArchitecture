package com.parthjpatel.cleanarchitecture.domain.repository

import com.parthjpatel.cleanarchitecture.domain.model.DomainModel

interface ImagesRepository {
    suspend fun getImages(q: String): Result<List<DomainModel>>
}