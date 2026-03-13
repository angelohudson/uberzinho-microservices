package com.uberzinho.riders.client

import com.uberzinho.riders.domain.RideUpdatedEvent
import org.springframework.graphql.client.HttpGraphQlClient
import org.springframework.stereotype.Component

@Component
class AppSyncClient(
    private val httpGraphQlClient: HttpGraphQlClient
) {
    object Key {
        const val UPDATE_RIDE_MUTATION = """
            mutation UpdateRideStatus(${'$'}input: RideInput!) {
                updateRideStatus(ride: ${'$'}input) {
                    driverId
                    status
                    rideId
                    driverName
                    location {
                        lat
                        lng
                    }
                }
            }
        """
    }

    fun updateStatus(input: RideUpdatedEvent): RideResponse? {
        return httpGraphQlClient.document(Key.UPDATE_RIDE_MUTATION.trimIndent())
            .variable("input", input)
            .retrieve("updateRideStatus")
            .toEntity(RideResponse::class.java)
            .block()
    }
}

data class RideUpdateInput(
    val driverId: String,
    val status: String,
    val location: LocationInput,
    val rideId: String
)

data class LocationInput(val lat: Double, val lng: Double)

data class RideResponse(
    val rideId: String,
    val driverId: String,
    val status: String,
    val location: LocationInput
)
