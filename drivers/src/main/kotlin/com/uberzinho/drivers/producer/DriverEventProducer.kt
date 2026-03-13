package com.uberzinho.drivers.producer

import com.fasterxml.jackson.databind.ObjectMapper
import com.uberzinho.drivers.domain.RideUpdateEvent
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class DriverEventProducer(
    @Value("\${spring.kafka.topics.ride-update}")
    private val rideUpdateTopic: String,
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectMapper: ObjectMapper = ObjectMapper()
) {

    fun publishRideUpdate(event: RideUpdateEvent) {
        val messageKey = event.rideId
        val message = objectMapper.writeValueAsString(event)
        kafkaTemplate.send(
            rideUpdateTopic,
            messageKey,
            message
        )
    }
}