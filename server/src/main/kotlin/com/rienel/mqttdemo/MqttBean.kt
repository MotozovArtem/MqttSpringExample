package com.rienel.mqttdemo

import com.fasterxml.jackson.databind.ObjectMapper
import io.rienel.mqttdemo.TOPIC_CLIENT_COMPUTER
import io.rienel.mqttdemo.model.ComputerStatus
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.integration.channel.DirectChannel
import org.springframework.integration.core.MessageProducer
import org.springframework.integration.mqtt.core.MqttPahoClientFactory
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter
import org.springframework.integration.mqtt.support.MqttHeaders
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel

/**
 * TODO ArMotozov
 *
 * @since 1/26/2023
 */
@Configuration
class MqttBean @Autowired constructor(
	val objectMapper: ObjectMapper,
	val configuration: MqttConfiguration,
	val clientFactory: MqttPahoClientFactory,
) {
	companion object {
		val log: Logger = LoggerFactory.getLogger(MqttBean::class.java)
	}

	@Bean(name = ["mqttInputChannel"])
	fun mqttInputChannel(): MessageChannel = DirectChannel()

	@Bean
	fun inbound(@Qualifier("mqttInputChannel") inputChannel: MessageChannel): MessageProducer =
		MqttPahoMessageDrivenChannelAdapter(configuration.clientId, clientFactory, TOPIC_CLIENT_COMPUTER).apply {
			setCompletionTimeout(5000)
			setConverter(DefaultPahoMessageConverter())
			setQos(2)
			outputChannel = inputChannel
		}

	@ServiceActivator(inputChannel = "mqttInputChannel")
	fun handler(message: Message<*>) {
		val topic = message.headers[MqttHeaders.RECEIVED_TOPIC].toString()
		if (topic == TOPIC_CLIENT_COMPUTER) {
			val computerStatus = objectMapper.readValue(message.payload.toString(), ComputerStatus::class.java)
			log.info("Payload: \n{}", computerStatus)
		}
	}
}