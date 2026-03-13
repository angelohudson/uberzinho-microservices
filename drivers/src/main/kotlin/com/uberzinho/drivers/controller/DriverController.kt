package com.uberzinho.drivers.controller

import com.uberzinho.drivers.domain.DriverInput
import com.uberzinho.drivers.domain.DriverLocationInput
import com.uberzinho.drivers.domain.DriverResponse
import com.uberzinho.drivers.domain.Location
import com.uberzinho.drivers.entity.Drivers
import com.uberzinho.drivers.service.DriverService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class DriverController(
    private val driverService: DriverService
) {

    @MutationMapping
    fun registerDriver(
        @Argument input: DriverInput
    ): DriverResponse {
        return driverService.registerDriver(input.name, input.location)
    }

    @MutationMapping
    fun updateDriverLocation(
        @Argument input: DriverLocationInput
    ): DriverResponse {
        return driverService.updateDriverLocation(input.driverId, input.location)
    }

    @QueryMapping
    fun findAvailableInArea(
        @Argument location: Location
    ): List<DriverResponse> {
        return driverService.findAvailableInArea(location.lat, location.lng)
    }
}