package com.moradyar.carouselfeature.core.model

data class ProductUnit (
    val id: String?,
    val name: String?,
    val position: Int? = -1,
    val price: String?,
    val url: String?
    //product: Product!
    //image: ProductImage!
)