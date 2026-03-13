package com.uberzinho.drivers.repository

import com.fasterxml.jackson.databind.ObjectMapper
import com.uberzinho.drivers.entity.Drivers
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.geo.Circle
import org.springframework.data.geo.Distance
import org.springframework.data.geo.Point
import org.springframework.data.redis.connection.RedisGeoCommands
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
class DriverRepository(
    @Value("\${redis.geo.key:drivers:locations}")
    val geoKey: String,
    val redisTemplate: RedisTemplate<String, String>,
    val objectMapper: ObjectMapper = ObjectMapper()
) {

    object Keys {
        const val MAX_DISTANCE_METERS = 5000.0
        const val DRIVER_DATA_PREFIX = "driver:data:"
    }

    fun findAvailableInArea(lat: Double, lgn: Double): List<Drivers> {
        val circle = Circle(Point(lgn, lat), Distance(Keys.MAX_DISTANCE_METERS, RedisGeoCommands.DistanceUnit.METERS))
        val geoResults = redisTemplate.opsForGeo().radius(geoKey, circle) ?: return emptyList()

        return geoResults.content
            .mapNotNull { findById(it.content.name) }
            .filter { it.available }
    }

    fun save(driver: Drivers): Drivers {
        redisTemplate.opsForGeo().add(geoKey, Point(driver.location.lng, driver.location.lat), driver.driverId)

        val driverJson = objectMapper.writeValueAsString(driver)
        redisTemplate.opsForValue().set("${Keys.DRIVER_DATA_PREFIX}${driver.driverId}", driverJson)

        return driver
    }

    fun findById(driverId: String): Drivers? {
        val json = redisTemplate.opsForValue().get("${Keys.DRIVER_DATA_PREFIX}$driverId")
        return json?.let { objectMapper.readValue(it, Drivers::class.java) }
    }
}