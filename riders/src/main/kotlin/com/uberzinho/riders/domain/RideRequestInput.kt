package com.uberzinho.riders.domain

data class RideRequestInput(
    val riderId: String,
    val pickup: Location,
    val dropoff: Location
)