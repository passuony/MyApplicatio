package com.example.myapplication.data.network.response

import com.google.gson.annotations.SerializedName

// الهيكل الرئيسي للرد من API
data class BooksApiResponse(
    @SerializedName("items") val items: List<VolumeItemDto>?
)

// يمثل كل كتاب (Volume) في الرد
data class VolumeItemDto(
    @SerializedName("id") val id: String?,
    @SerializedName("volumeInfo") val volumeInfo: VolumeInfoDto?
)

// معلومات مفصلة عن الكتاب (مثل العنوان والمؤلف)
data class VolumeInfoDto(
    @SerializedName("title") val title: String?,
    @SerializedName("authors") val authors: List<String>?,
    @SerializedName("publishedDate") val publishedDate: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("pageCount") val pageCount: Int?,
    @SerializedName("categories") val categories: List<String>?,
    @SerializedName("imageLinks") val imageLinks: ImageLinksDto?
)

// روابط الصور (الغلاف)
data class ImageLinksDto(
    @SerializedName("thumbnail") val thumbnail: String?
)