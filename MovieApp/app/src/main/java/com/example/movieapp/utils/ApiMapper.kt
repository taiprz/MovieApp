package com.example.movieapp.utils

// API mapper
interface ApiMapper<Domain, Entity> {
    fun mapToDomain(apiDto:Entity):Domain
}