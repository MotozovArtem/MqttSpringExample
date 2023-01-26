package com.rienel.mqttdemo

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
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.MessageHandler

/**
 * TODO ArMotozov
 *
 * @since 1/26/2023
 */
@Configuration
class MqttBean @Autowired constructor(
	val configuration: MqttConfiguration,
	val clientFactory: MqttPahoClientFactory
) {
	companion object {
		val log: Logger = LoggerFactory.getLogger(MqttBean::class.java)
	}

	@Bean(name = ["mqttInputChannel"])
	fun mqttInputChannel(): MessageChannel = DirectChannel()

	@Bean
	fun inbound(@Qualifier("mqttInputChannel") inputChannel: MessageChannel): MessageProducer =
		MqttPahoMessageDrivenChannelAdapter(configuration.clientId, clientFactory, "#").apply {
			setCompletionTimeout(5000)
			setConverter(DefaultPahoMessageConverter())
			setQos(2)
			outputChannel = inputChannel
		}

	@Bean
	@ServiceActivator(inputChannel = "mqttInputChannel")
	fun handler(): MessageHandler = MessageHandler {
		val topic = it.headers[MqttHeaders.RECEIVED_TOPIC].toString()
		if (topic == "testTopic") {
			log.info("This is test topic")
		}
		log.info("Payload: {}", it.payload)
	}
}