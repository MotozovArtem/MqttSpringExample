package com.rienel.mqttdemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MqttdemoApplication

fun main(args: Array<String>) {
	runApplication<MqttdemoApplication>(*args)
}
