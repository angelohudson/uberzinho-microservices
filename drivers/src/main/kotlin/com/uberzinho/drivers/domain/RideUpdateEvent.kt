package com.uberzinho.drivers.domain

data class RideUpdateEvent(
    val rideId: String,
    val driverId: String,
    val driverName: String,
    val location: Location,
    val status: RideStatus
)
