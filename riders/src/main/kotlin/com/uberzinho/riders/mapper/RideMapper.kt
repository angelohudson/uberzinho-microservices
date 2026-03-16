package com.uberzinho.riders.mapper

import com.uberzinho.riders.domain.RideRequestInput
import com.uberzinho.riders.domain.RideRequestedEvent
import com.uberzinho.riders.domain.RideUpdatedEvent
import com.uberzinho.riders.entity.Location
import com.uberzinho.riders.entity.RideStatus
import com.uberzinho.riders.entity.Riders
import com.uberzinho.riders.entity.Rides

object RideMapper {
    fun toEntity(event: RideUpdatedEvent): Rides = Rides(
        rideId = event.rideId,
        driverId = event.driverId,
        location = event.location?.let {
            Location(
                lat = it.lat,
                lng = it.lng
            )
        },
        status = event.status?.let { RideStatus.valueOf(it.name) }
    )

    fun toRiderEntity(input: RideRequestInput, rideId: String): Riders = Riders(
        riderId = input.riderId,
        currentRideId = rideId,
        inRide = true,
        location = Location(
            lat = input.pickup.lat,
            lng = input.pickup.lng
        )
    )

    fun toRideEntity(input: RideRequestInput, rideId: String): Rides = Rides(
        rideId = rideId,
        riderId = input.riderId,
        driverId = "",
        location = Location(
            lat = input.pickup.lat,
            lng = input.pickup.lng
        ),
        status = RideStatus.REQUESTED
    )

    fun toEvent(input: RideRequestInput, rideId: String): RideRequestedEvent = RideRequestedEvent(
        rideId,
        riderId = input.riderId,
        pickup = input.pickup,
        dropoff = input.dropoff
    )
}
