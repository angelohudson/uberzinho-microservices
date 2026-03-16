package com.uberzinho.riders.repository

import com.uberzinho.riders.entity.Rides
import io.awspring.cloud.dynamodb.DynamoDbTemplate
import io.netty.util.internal.StringUtil
import org.springframework.stereotype.Repository
import software.amazon.awssdk.enhanced.dynamodb.Key

@Repository
class RideRepository(
    val dynamoDbTemplate: DynamoDbTemplate
) {
    fun update(updatedRide: Rides): Rides {
        if (updatedRide.rideId == null) throw RuntimeException("Ride ID is required")
        val ride = this.findById(updatedRide.rideId!!) ?: throw RuntimeException("Ride not found")
        ride.riderId = if (StringUtil.isNullOrEmpty(updatedRide.riderId)) updatedRide.riderId else ride.riderId
        ride.driverId = if (StringUtil.isNullOrEmpty(updatedRide.driverId)) updatedRide.driverId else ride.driverId
        ride.status = if (updatedRide.status != null) updatedRide.status else ride.status
        ride.location = if (updatedRide.location != null) updatedRide.location else ride.location
        return this.save(ride)
    }

    fun save(ride: Rides): Rides {
        return dynamoDbTemplate.save(ride)
    }

    fun findById(rideId: String): Rides? {
        return dynamoDbTemplate.load(
            Key.builder()
                .partitionValue(rideId)
                .build(), Rides::class.java
        )
    }
}
