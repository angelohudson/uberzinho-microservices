package com.uberzinho.drivers.client

import org.springframework.graphql.client.HttpGraphQlClient
import org.springframework.stereotype.Component

@Component
class AppSyncClient(
    private val httpGraphQlClient: HttpGraphQlClient
) {
    object Key {
        const val ASSIGN_RIDE_MUTATION = """
            mutation AssignRide(${'$'}input: RideInput!) {
              assignRide(
                ride: ${'$'}input
              ) {
                dropoff {
                  lat
                  lng
                }
                pickup {
                  lat
                  lng
                }
                rideId
                driverId
              }
            }
        """
    }

    fun assignRide(input: RideAssignedEvent): RideResponse? {
        return httpGraphQlClient.document(Key.ASSIGN_RIDE_MUTATION.trimIndent())
            .variable("input", input)
            .retrieve("assignRide")
            .toEntity(RideResponse::class.java)
            .block()
    }
}

data class RideAssignedEvent(
    val rideId: String,
    val driverId: String,
    val pickup: LocationInput,
    val dropoff: LocationInput
)

data class LocationInput(val lat: Double, val lng: Double)

data class RideResponse(
    val rideId: String,
    val driverId: String? = null,
    val status: String? = null,
    val location: LocationInput? = null
)
