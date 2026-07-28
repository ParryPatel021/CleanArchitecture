package com.parthjpatel.cleanarchitecture.data.repository

import com.parthjpatel.cleanarchitecture.data.remote.ApiService
import com.parthjpatel.cleanarchitecture.data.remote.RetrofitInstance
import com.parthjpatel.cleanarchitecture.domain.model.DomainModel
import com.parthjpatel.cleanarchitecture.domain.repository.ImagesRepository

class ImagesRepositoryImpl : ImagesRepository {

    private val apiService: ApiService by lazy { RetrofitInstance.getApiService() }

    override suspend fun getImages(q: String): Result<List<DomainModel>> {
        return try {
            val response = apiService.getImages(q = q)
            val listOfImages = response.hits.map { DomainModel(it.previewURL) }
            Result.success(listOfImages)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}