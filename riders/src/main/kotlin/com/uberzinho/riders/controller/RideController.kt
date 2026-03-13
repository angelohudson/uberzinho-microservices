package com.uberzinho.riders.controller

import com.uberzinho.riders.domain.RideRequestInput
import com.uberzinho.riders.domain.RideResponse
import com.uberzinho.riders.service.RideService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class RideController(
    private val rideService: RideService
) {
    @MutationMapping
    fun requestRide(@Argument input: RideRequestInput): RideResponse {
        return rideService.requestRide(
            riderId = input.riderId,
            pickup = input.pickup,
            dropoff = input.dropoff
        )
    }

    @QueryMapping
    fun getRideById(@Argument rideId: String): RideResponse {
        return rideService.getRideById(rideId)
    }
}
