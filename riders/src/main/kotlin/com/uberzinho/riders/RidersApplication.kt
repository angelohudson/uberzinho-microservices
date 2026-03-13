package com.uberzinho.riders

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.kafka.annotation.EnableKafka

@SpringBootApplication
@EnableKafka
class RidersApplication

fun main(args: Array<String>) {
    runApplication<RidersApplication>(*args)
}
