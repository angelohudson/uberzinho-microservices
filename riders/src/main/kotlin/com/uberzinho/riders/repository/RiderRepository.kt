package com.uberzinho.riders.repository

import com.uberzinho.riders.entity.Riders
import com.uberzinho.riders.entity.Rides
import io.awspring.cloud.dynamodb.DynamoDbTemplate
import io.netty.util.internal.StringUtil
import org.springframework.stereotype.Repository
import software.amazon.awssdk.enhanced.dynamodb.Key

@Repository
class RiderRepository(
    val dynamoDbTemplate: DynamoDbTemplate
) {
    fun save(rider: Riders): Riders {
        return dynamoDbTemplate.save(rider)
    }

    fun findById(riderId: String): Riders? {
        return dynamoDbTemplate.load(
            Key.builder()
                .partitionValue(riderId)
                .build(), Riders::class.java
        )
    }
}
