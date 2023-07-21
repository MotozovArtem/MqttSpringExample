package com.rienel.mqttdemo

import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory

/**
 * MQTT Credentials Configuration
 *
 * @since 1/26/2023
 */
@Configuration
data class MqttConfiguration(
	var host: String = "localhost",
	var port: Int = 1883,
	var username: String = "user",
	var password: String = "user", // bad practice to save password in sources
	var clientId: String = "server"
) {
	@Bean
	fun mqttConnectOptions() = MqttConnectOptions().apply {
		serverURIs = arrayOf("tcp://${host.trim()}:$port")
		username = this@MqttConfiguration.username
		password = this@MqttConfiguration.password.toCharArray()
		isAutomaticReconnect = true
		keepAliveInterval = 10
	}

	@Bean
	fun mqttClientFactory(connectionOptionsBean: MqttConnectOptions) = DefaultMqttPahoClientFactory().apply {
		connectionOptions = connectionOptionsBean
	}
}