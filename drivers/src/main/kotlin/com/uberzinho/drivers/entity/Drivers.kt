package com.uberzinho.drivers.entity

data class Drivers(
    var driverId: String,
    var name: String,
    var rideId: String? = null,
    var available: Boolean,
    var location: DriverLocation
)

data class DriverLocation(
    var lat: Double,
    var lng: Double
)