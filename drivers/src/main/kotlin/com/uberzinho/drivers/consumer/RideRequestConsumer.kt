package com.uberzinho.drivers.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.uberzinho.drivers.domain.RideRequestedEvent
import com.uberzinho.drivers.service.DriverService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class RideRequestConsumer(
    private val driverService: DriverService,
    private val objectMapper: ObjectMapper = ObjectMapper()
) {
    @KafkaListener(
        topics = ["\${spring.kafka.topics.ride-requested}"],
        groupId = "\${spring.kafka.consumer.group-id}"
    )
    fun consume(event: String) {
        val rideRequestedEvent = objectMapper.readValue(event, RideRequestedEvent::class.java)
        driverService.findClosestDriver(rideRequestedEvent)
    }
}
