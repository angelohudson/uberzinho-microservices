package com.uberzinho.riders.service

import com.uberzinho.riders.client.AppSyncClient
import com.uberzinho.riders.domain.*
import com.uberzinho.riders.mapper.RideMapper
import com.uberzinho.riders.producer.RideEventProducer
import com.uberzinho.riders.repository.RideRepository
import com.uberzinho.riders.repository.RiderRepository
import org.springframework.stereotype.Service
import java.util.*
import com.uberzinho.riders.entity.RideStatus as RideStatusEntity

@Service
class RideService(
    private val rideRepository: RideRepository,
    private val riderRepository: RiderRepository,
    private val rideEventProducer: RideEventProducer,
    private val appSyncClient: AppSyncClient
) {
    fun requestRide(input: RideRequestInput): RideResponse {
        val rideId = UUID.randomUUID().toString()
        rideRepository.save(RideMapper.toRideEntity(input, rideId))
        riderRepository.save(RideMapper.toRiderEntity(input, rideId))
        rideEventProducer.sendRideRequestedEvent(RideMapper.toEvent(input, rideId))
        return RideResponse(
            rideId = rideId,
            status = RideStatus.REQUESTED
        )
    }

    fun updateRide(event: RideUpdatedEvent) {
        val ride = RideMapper.toEntity(event)
        this.rideRepository.update(ride)
        this.appSyncClient.updateStatus(event)
    }

    fun cancelRide(rideId: String): RideResponse {
        val ride = this.rideRepository.findById(rideId) ?: throw RuntimeException("Ride not found")
        ride.status = RideStatusEntity.CANCELLED
        this.rideRepository.save(ride)
        this.rideEventProducer.sendCancelRideEvent(RideCancelEvent(rideId, ride.driverId ?: ""))
        this.appSyncClient.updateStatus(rideId, RideStatus.CANCELLED)
        return RideResponse(
            rideId = ride.rideId ?: "",
            status = RideStatus.CANCELLED
        )
    }

    fun getActiveRideByRider(riderId: String): RideResponse? {
        val rider = this.riderRepository.findById(riderId) ?: return null
        if (!rider.inRide || rider.currentRideId == null) return null
        val ride = rider.currentRideId?.let { this.rideRepository.findById(it) } ?: return null
        return RideResponse(
            rideId = rider.currentRideId,
            status = ride.status?.let { RideStatus.valueOf(it.name) },
            driverId = ride.driverId,
            location = ride.location?.let {
                Location(
                    lat = it.lat,
                    lng = it.lng
                )
            }
        )
    }

    fun getRideById(rideId: String): RideResponse {
        val ride = this.rideRepository.findById(rideId) ?: throw RuntimeException("Ride not found")
        return RideResponse(
            rideId = ride.rideId,
            status = ride.status?.let { RideStatus.valueOf(it.name) },
            driverId = ride.driverId,
            location = ride.location?.let {
                Location(
                    lat = it.lat,
                    lng = it.lng
                )
            }
        )
    }
}
