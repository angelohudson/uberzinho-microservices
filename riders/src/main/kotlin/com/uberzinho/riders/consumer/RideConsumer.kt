package com.uberzinho.riders.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.uberzinho.riders.domain.RideUpdatedEvent
import com.uberzinho.riders.service.RideService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import kotlin.jvm.javaClass

@Component
class RideConsumer(
    private val rideService: RideService,
    private val objectMapper: ObjectMapper = ObjectMapper(),
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @KafkaListener(
        topics = ["\${spring.kafka.topics.ride-update}"],
        groupId = "\${spring.kafka.consumer.group-id}"
    )
    fun consume(event: String) {
        logger.info("Received ride update event: $event")
        val rideUpdatedEvent: RideUpdatedEvent = objectMapper.readValue(event, RideUpdatedEvent::class.java)
        rideService.updateRide(rideUpdatedEvent)
    }

}
