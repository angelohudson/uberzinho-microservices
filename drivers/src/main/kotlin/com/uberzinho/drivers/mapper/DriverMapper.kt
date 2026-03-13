package com.uberzinho.drivers.mapper

import com.uberzinho.drivers.domain.DriverResponse
import com.uberzinho.drivers.domain.Location
import com.uberzinho.drivers.entity.Drivers

object DriverMapper {

    fun toResponse(driver: Drivers): DriverResponse {
        return DriverResponse(
            driverId = driver.driverId,
            name = driver.name,
            location = Location(
                lat = driver.location.lat,
                lng = driver.location.lng
            ),
            available = driver.available
        )
    }

}