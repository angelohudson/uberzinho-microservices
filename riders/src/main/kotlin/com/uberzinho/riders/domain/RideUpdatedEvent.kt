package com.uberzinho.riders.domain

data class RideUpdatedEvent(
    val rideId: String,
    val driverId: String,
    val driverName: String,
    val location: Location,
    val status: RideStatus
)
