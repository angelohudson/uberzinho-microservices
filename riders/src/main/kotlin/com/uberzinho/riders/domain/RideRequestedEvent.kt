package com.uberzinho.riders.domain

data class RideRequestedEvent(
    val rideId: String,
    val riderId: String,
    val pickup: Location,
    val dropoff: Location
)