package com.uberzinho.riders.interceptor

import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.listener.RecordInterceptor
import org.springframework.stereotype.Component

@Component
class GlobalConsumerInterceptor : RecordInterceptor<Any, Any> {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun intercept(
        record: ConsumerRecord<Any, Any>,
        consumer: Consumer<Any, Any>
    ): ConsumerRecord<Any, Any>? {
        logger.info("Processing topic: ${record.topic()} | Offset: ${record.offset()}")
        return record
    }
}
