package com.uberzinho.riders.interceptor

import org.slf4j.LoggerFactory
import org.springframework.graphql.server.WebGraphQlInterceptor
import org.springframework.graphql.server.WebGraphQlRequest
import org.springframework.graphql.server.WebGraphQlResponse
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GraphQlLoggingInterceptor : WebGraphQlInterceptor {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun intercept(request: WebGraphQlRequest, chain: WebGraphQlInterceptor.Chain): Mono<WebGraphQlResponse> {
        val query = request.document.replace(Regex("\\s+"), " ").trim()
        val variables = request.variables
        val operationName = request.operationName ?: "unnamed"

        logger.info("GraphQL Request: op={}, query=[{}], vars={}", operationName, query, variables)

        return chain.next(request).doOnNext { response ->
            if (response.errors.isNotEmpty()) {
                logger.error("GraphQL Errors for {}: {}", operationName, response.errors)
            }
        }
    }
}
