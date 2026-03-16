package com.uberzinho.drivers.service

import com.uberzinho.drivers.client.AppSyncClient
import com.uberzinho.drivers.client.LocationInput
import com.uberzinho.drivers.client.RideAssignedEvent
import com.uberzinho.drivers.domain.*
import com.uberzinho.drivers.entity.DriverLocation
import com.uberzinho.drivers.entity.Drivers
import com.uberzinho.drivers.mapper.DriverMapper
import com.uberzinho.drivers.producer.DriverEventProducer
import com.uberzinho.drivers.repository.DriverRepository
import com.uberzinho.drivers.utils.GeoUtils
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class DriverService(
    private val driverEventProducer: DriverEventProducer,
    private val driverRepository: DriverRepository,
    private val appSyncClient: AppSyncClient
) {

    fun findClosestDriver(
        event: RideRequestedEvent
    ) {
        // getting the closest driver in the pickup area
        val drivers = driverRepository
            .findAvailableInArea(event.pickup.lat, event.pickup.lng)

        if (drivers.isEmpty()) return

        // finding the closest driver to the pickup location
        val closest = drivers.minByOrNull {
            GeoUtils.distanceKm(
                event.pickup.lat,
                event.pickup.lng,
                it.location.lat,
                it.location.lng
            )
        } ?: return
        closest.rideId = event.rideId
        closest.available = false

        // publishing ride update event to notify the rider about the assigned driver
        this.publishRideUpdate(closest)

        this.appSyncClient.assignRide(
            RideAssignedEvent(
                rideId = event.rideId,
                driverId = closest.driverId,
                pickup = LocationInput(
                    lat = event.pickup.lat,
                    lng = event.pickup.lng
                ),
                dropoff = LocationInput(
                    lat = event.dropoff.lat,
                    lng = event.dropoff.lng
                )
            )
        )

        // marking the driver as unavailable and saving the assigned rideId
        driverRepository.save(closest)
    }


    fun findAvailableInArea(lat: Double, lgn: Double): List<DriverResponse> {
        return driverRepository.findAvailableInArea(lat, lgn)
            .map { DriverMapper.toResponse(it) }
    }

    fun updateDriverLocation(driverId: String, location: Location): DriverResponse {
        val existingDriver: Drivers =
            driverRepository.findById(driverId)
                ?: throw IllegalArgumentException("Driver not found with id: $driverId")

        existingDriver.location = location.let {
            DriverLocation(
                lat = it.lat,
                lng = it.lng
            )
        }

        driverRepository.save(existingDriver)

        if (existingDriver.rideId != null)
            this.publishRideUpdate(existingDriver)

        return DriverMapper.toResponse(existingDriver)
    }

    fun registerDriver(name: String, location: Location): DriverResponse {
        val driver = Drivers(
            driverId = UUID.randomUUID().toString(),
            name = name,
            available = true,
            location = DriverLocation(
                lat = location.lat,
                lng = location.lng
            )
        )
        return driverRepository.save(driver)
            .let { DriverMapper.toResponse(it) }
    }

    fun getById(driverId: String): DriverResponse {
        val driver: Drivers =
            driverRepository.findById(driverId) ?: throw IllegalArgumentException("Driver not found with id: $driverId")
        return driver
            .let { DriverMapper.toResponse(it) }
    }

    fun cancelRide(event: RideCancelEvent) {
        val driver = driverRepository.findById(event.driverId) ?: return
        driver.rideId = null
        driver.available = true
        driverRepository.save(driver)
    }

    private fun publishRideUpdate(driver: Drivers) {
        val rideUpdateEvent = RideUpdateEvent(
            rideId = driver.rideId ?: "",
            driverId = driver.driverId,
            driverName = driver.name,
            location = driver.location.let {
                Location(
                    lat = it.lat,
                    lng = it.lng
                )
            },
            status = RideStatus.DRIVER_ASSIGNED,
        )
        driverEventProducer.publishRideUpdate(rideUpdateEvent)
    }
}
