package com.uberzinho.riders.domain

data class RideUpdatedEvent(
    val rideId: String,
    val driverId: String? = null,
    val driverName: String? = null,
    val location: Location? = null,
    val status: RideStatus?
)
