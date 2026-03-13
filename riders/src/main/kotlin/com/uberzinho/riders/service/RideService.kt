package com.uberzinho.riders.service

import com.uberzinho.riders.client.AppSyncClient
import com.uberzinho.riders.domain.*
import com.uberzinho.riders.entity.Rides
import com.uberzinho.riders.mapper.RideMapper
import com.uberzinho.riders.producer.RideEventProducer
import com.uberzinho.riders.repository.RideRepository
import org.springframework.stereotype.Service
import java.util.UUID
import com.uberzinho.riders.entity.RideStatus as RideStatusEntity
import com.uberzinho.riders.entity.Location as LocationEntity

@Service
class RideService(
    private val rideRepository: RideRepository,
    private val rideEventProducer: RideEventProducer,
    private val appSyncClient: AppSyncClient
) {
    fun requestRide(riderId: String, pickup: Location, dropoff: Location): RideResponse {
        val rideId = UUID.randomUUID().toString()

        val event = RideRequestedEvent(
            rideId,
            riderId = riderId,
            pickup = pickup,
            dropoff = dropoff
        )

        rideRepository.save(
            Rides(
                rideId = rideId,
                driverId = "",
                location = LocationEntity(
                    lat = pickup.lat,
                    lng = pickup.lng
                ),
                status = RideStatusEntity.REQUESTED
            )
        )

        rideEventProducer.sendRideRequestedEvent(event)

        return RideResponse(
            rideId = rideId,
            status = RideStatus.REQUESTED
        )
    }

    fun updateRide(event: RideUpdatedEvent) {
        val ride = RideMapper.toEntity(event)
        this.rideRepository.save(ride)
        this.appSyncClient.updateStatus(event)
    }

    fun getRideById(rideId: String): RideResponse {
        val ride = this.rideRepository.findById(rideId) ?: throw RuntimeException("Ride not found")
        return RideResponse(
            rideId = ride.rideId,
            status = ride.status?.let { RideStatus.valueOf(it.name) },
            driverId = if (ride.driverId.isBlank()) null else ride.driverId,
            location = ride.location?.let {
                Location(
                    lat = it.lat,
                    lng = it.lng
                )
            }
        )
    }
}