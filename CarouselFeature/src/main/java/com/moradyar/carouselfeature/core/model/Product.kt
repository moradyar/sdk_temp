package com.moradyar.carouselfeature.core.model

data class Product(
    val id: String?,
    val name: String?,
    val currency: String?,
    val description: String?,
    val options: List<String>?,
    val images: List<ProductImage>?,
    val units: List<ProductUnit>?
)