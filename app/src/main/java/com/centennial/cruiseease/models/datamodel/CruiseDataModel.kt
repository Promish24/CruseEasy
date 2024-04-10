package com.centennial.cruiseease.models.datamodel

data class PhotoGallery(
    var image1: String? = "",
    var image2: String? = "",
    var image3: String? = "",
    var image4: String? = "",
    var image5: String? = "",
    var image6: String? = "",
    var image7: String? = "",
    var image8: String? = "",
    var image9: String? = "",
    var image10: String? = ""
)

data class RoomType(
    var name: String? = "",
    var rate: Int? = 0
)

data class Featured(
    var featuredName: String? = "",
    var from: String? = "",
    var interiorFrom: Int? = 0,
    var photoGallery: PhotoGallery = PhotoGallery(),
    var ports: String? = "",
    var way: String? = "",
    var roomtypes: List<RoomType> = listOf()
) {
    constructor() : this("", "", 0, PhotoGallery(), "", "", listOf())
}

data class Cruise(
    var background: String? = "",
    var description: String? = "",
    val packages: List<Featured> = listOf(),
    var name: String? = "",
    var rating: Double? = 0.0,
    var ratingMax: Int? = 0,
    var startingFrom: Int? = 0,
    var id: String? = ""
) {
    constructor() : this("", "", listOf(), "", 0.0, 0, 0)
}

