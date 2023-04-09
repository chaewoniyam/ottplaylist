package com.example.testapp

class MovieProfiles(val posterImageUrl: String? = null, val movieName: String? = null, val movieGenreAndYear: String? = null, val toInfoUrl: String? = null) {
    constructor() : this(null, null, null, null)
}
