package com.uberzinho.drivers.domain

data class RideCancelEvent(
    val rideId: String,
    val driverId: String
)
