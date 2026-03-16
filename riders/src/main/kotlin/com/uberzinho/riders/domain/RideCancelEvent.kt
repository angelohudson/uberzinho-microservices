package com.uberzinho.riders.domain

data class RideCancelEvent(
    val rideId: String,
    val driverId: String
)
