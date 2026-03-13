package com.uberzinho.riders.mapper

import com.uberzinho.riders.domain.RideUpdatedEvent
import com.uberzinho.riders.entity.Location
import com.uberzinho.riders.entity.RideStatus
import com.uberzinho.riders.entity.Rides

object RideMapper {
    fun toEntity(event: RideUpdatedEvent): Rides {
        return Rides(
            rideId = event.rideId,
            driverId = event.driverId,
            location = event.location.let {
                Location(
                    lat = it.lat,
                    lng = it.lng
                )
            },
            status = RideStatus.valueOf(event.status.name)
        )
    }
}