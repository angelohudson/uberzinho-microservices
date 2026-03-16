package com.uberzinho.riders.entity

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey

@DynamoDbBean
data class Rides(
    @get:DynamoDbPartitionKey
    var rideId: String? = null,
    var riderId: String? = null,
    var driverId: String? = null,
    var status: RideStatus? = null,
    var location: Location? = null,
)

@DynamoDbBean
data class Location(
    var lat: Double = 0.0,
    var lng: Double = 0.0
)

enum class RideStatus {
    REQUESTED,
    SEARCHING,
    DRIVER_ASSIGNED,
    ARRIVING,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED,
}
