package com.parthjpatel.cleanarchitecture.data.model

data class PixabayResponse(
    val hits: List<Hits>,
    val total: Int,
    val totalHits: Int,
)
