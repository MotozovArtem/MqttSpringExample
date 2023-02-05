package io.rienel.mqttdemo.client

import com.fasterxml.jackson.databind.ObjectMapper
import io.rienel.mqttdemo.TOPIC_CLIENT_COMPUTER
import io.rienel.mqttdemo.model.ComputerStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.eclipse.paho.client.mqttv3.IMqttClient
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.util.UUID

/**
 * MQTT Client example
 *
 * @since 2/5/2023
 */
suspend fun main() {
	val publisherId = UUID.randomUUID().toString()
	val client: IMqttClient = MqttClient("tcp://192.168.1.7:1883", publisherId)
	val connectOptions  = MqttConnectOptions().apply {
		connectionTimeout = 10
		isAutomaticReconnect = true
		isCleanSession = true
	}
	client.connect(connectOptions)
	val systemProperties = System.getProperties()
	val objectMapper = ObjectMapper()
	while (true) {
		val data = ComputerStatus(
			userName =  systemProperties["user.name"]?.toString() ?: "UNKNOWN",
			path = System.getenv("PATH") ?: "UNKNOWN",
			systemTimeMillis = System.currentTimeMillis(),
			javaVersion = systemProperties["java.version"]?.toString() ?: "UNKNOWN"
		)
		println("Sending message: $data")
		val payload = objectMapper.writeValueAsBytes(data)
		val msg = MqttMessage(payload)
		msg.qos = 0
		msg.isRetained = true
		client.publish(TOPIC_CLIENT_COMPUTER, msg)
		delay(2000L)
	}
}