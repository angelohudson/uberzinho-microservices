package com.uberzinho.riders.domain

data class RideAssignedEvent(
    val rideId: String,
    val driverId: String? = null,
    val pickup: Location? = null,
    val dropoff: Location? = null
)
