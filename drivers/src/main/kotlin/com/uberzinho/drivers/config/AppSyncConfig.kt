package com.uberzinho.drivers.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.graphql.client.HttpGraphQlClient

@Configuration
class AppSyncConfig(
    @Value("\${appsync.api-key}") private val apiKey: String,
    @Value("\${appsync.url}") private val url: String
) {
    @Bean
    fun httpGraphQlClient(): HttpGraphQlClient {
        return HttpGraphQlClient.builder()
            .url(url)
            .header("x-api-key", apiKey)
            .build()
    }
}
