package com.uberzinho.riders.repository

import com.uberzinho.riders.entity.Rides
import io.awspring.cloud.dynamodb.DynamoDbTemplate
import org.springframework.stereotype.Repository
import software.amazon.awssdk.enhanced.dynamodb.Key

@Repository
class RideRepository(
    val dynamoDbTemplate: DynamoDbTemplate
) {
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