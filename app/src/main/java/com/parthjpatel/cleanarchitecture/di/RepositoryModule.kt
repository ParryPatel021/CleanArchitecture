package com.parthjpatel.cleanarchitecture.di

import com.parthjpatel.cleanarchitecture.data.repository.ImagesRepositoryImpl
import com.parthjpatel.cleanarchitecture.domain.repository.ImagesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindImagesRepository(imagesRepositoryImpl: ImagesRepositoryImpl): ImagesRepository
}