package com.uberzinho.riders.domain

data class RideResponse(
    val rideId: String,
    val status: RideStatus?,
    val driverId: String? = null,
    val location: Location? = null
)