package com.uberzinho.riders.entity

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey

@DynamoDbBean
data class Riders(
    @get:DynamoDbPartitionKey
    var riderId: String? = null,
    var currentRideId: String? = null,
    var inRide: Boolean = false,
    var location: Location? = null,
)
