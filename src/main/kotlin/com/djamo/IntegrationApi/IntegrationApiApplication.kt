package com.djamo.IntegrationApi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan("com.djamo.IntegrationApi")
class IntegrationApiApplication

fun main(args: Array<String>) {
	runApplication<IntegrationApiApplication>(*args)
}
