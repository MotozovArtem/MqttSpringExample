package com.rienel.mqttdemo

import io.moquette.BrokerConstants
import io.moquette.broker.Server
import io.moquette.broker.config.ClasspathResourceLoader
import io.moquette.broker.config.IConfig
import io.moquette.broker.config.IResourceLoader
import io.moquette.broker.config.ResourceLoaderConfig
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import kotlin.system.exitProcess

@SpringBootApplication
class MqttDemoApplication {
	companion object {
		val log: Logger = LoggerFactory.getLogger(MqttDemoApplication::class.java)
	}
}

fun main(args: Array<String>) {
	startMoquette()
	runApplication<MqttDemoApplication>(*args)
}

fun startMoquette() {
	val classpathLoader: IResourceLoader = ClasspathResourceLoader()
	val classPathConfig: IConfig = ResourceLoaderConfig(classpathLoader)
	classPathConfig.setProperty(BrokerConstants.ALLOW_ZERO_BYTE_CLIENT_ID_PROPERTY_NAME, "true")


	val mqttBroker = Server()
	try {
		mqttBroker.startServer(classPathConfig)
	} catch (e: Exception) {
		MqttDemoApplication.log.error("Cannot start MQTT Broker. Shutting down application", e)
		exitProcess(0)
	}
	Runtime.getRuntime().addShutdownHook(Thread {
		mqttBroker.stopServer();
		MqttDemoApplication.log.info("Embedded MQTT Broker stopped.")
	})
}
