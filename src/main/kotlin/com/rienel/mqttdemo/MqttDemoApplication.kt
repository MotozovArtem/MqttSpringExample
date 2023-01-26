package com.rienel.mqttdemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MqttDemoApplication

fun main(args: Array<String>) {
	runApplication<MqttDemoApplication>(*args)
}
