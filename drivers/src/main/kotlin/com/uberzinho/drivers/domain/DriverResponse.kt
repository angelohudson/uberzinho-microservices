package com.uberzinho.drivers.domain

data class DriverResponse(
    val driverId: String,
    val name: String,
    val location: Location,
    val available: Boolean,
    val associatedRide: RideResponse?
)

data class RideResponse(
    val rideId: String?,
)
