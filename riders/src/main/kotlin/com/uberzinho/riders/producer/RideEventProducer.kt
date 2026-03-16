package com.uberzinho.riders.producer

import com.fasterxml.jackson.databind.ObjectMapper
import com.uberzinho.riders.domain.RideCancelEvent
import com.uberzinho.riders.domain.RideRequestedEvent
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class RideEventProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectMapper: ObjectMapper = ObjectMapper()
) {
    @Value("\${spring.kafka.topics.ride-requested}")
    private lateinit var rideRequestedTopic: String

    @Value("\${spring.kafka.topics.cancel-ride}")
    private lateinit var cancelRideTopic: String

    fun sendRideRequestedEvent(event: RideRequestedEvent) {
        val messageKey = event.rideId
        val message = objectMapper.writeValueAsString(event)
        kafkaTemplate.send(rideRequestedTopic, messageKey, message)
    }

    fun sendCancelRideEvent(event: RideCancelEvent) {
        val message = objectMapper.writeValueAsString(event)
        kafkaTemplate.send(cancelRideTopic, event.rideId, message)
    }
}
