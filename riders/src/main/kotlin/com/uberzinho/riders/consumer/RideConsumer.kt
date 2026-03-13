package com.uberzinho.riders.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.uberzinho.riders.domain.RideUpdatedEvent
import com.uberzinho.riders.service.RideService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class RideConsumer(
    private val rideService: RideService,
    private val objectMapper: ObjectMapper = ObjectMapper()
) {

    @KafkaListener(
        topics = ["\${spring.kafka.topics.ride-update}"],
        groupId = "\${spring.kafka.consumer.group-id}"
    )
    fun consume(event: String) {
        val rideUpdatedEvent: RideUpdatedEvent = objectMapper.readValue(event, RideUpdatedEvent::class.java)
        rideService.updateRide(rideUpdatedEvent)
    }

}