package com.uberzinho.drivers.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.uberzinho.drivers.domain.RideCancelEvent
import com.uberzinho.drivers.service.DriverService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class RideCancelConsumer(
    val driverService: DriverService,
    val objectMapper: ObjectMapper

) {

    @KafkaListener(
        topics = ["\${spring.kafka.topics.cancel-ride}"],
        groupId = "\${spring.kafka.consumer.group-id}"
    )
    fun consume(event: String) {
        val event = objectMapper.readValue(event, RideCancelEvent::class.java)
        driverService.cancelRide(event)
    }

}
