package com.uberzinho.riders.config

import com.uberzinho.riders.interceptor.GlobalConsumerInterceptor
import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory

@Configuration
@EnableKafka
class KafkaConfig(
    private val properties: KafkaProperties
) {
    @Bean
    @ConditionalOnMissingBean(name = ["kafkaListenerContainerFactory"])
    fun kafkaListenerContainerFactory(
        configure: ConcurrentKafkaListenerContainerFactoryConfigurer,
        consumerFactory: ObjectProvider<ConsumerFactory<Any, Any>>,
        globalInterceptor: GlobalConsumerInterceptor
    ): ConcurrentKafkaListenerContainerFactory<Any, Any> {
        val factory = ConcurrentKafkaListenerContainerFactory<Any, Any>()
        configure.configure(
            factory,
            consumerFactory.getIfAvailable { DefaultKafkaConsumerFactory(this.properties.buildConsumerProperties()) })
        factory.setConcurrency(3)
        factory.setRecordInterceptor(globalInterceptor)
        return factory
    }
}
