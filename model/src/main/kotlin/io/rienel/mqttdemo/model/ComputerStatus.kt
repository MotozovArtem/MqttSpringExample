package io.rienel.mqttdemo.model

/**
 * Computer status model data class
 *
 * @since 2/5/2023
 */
data class ComputerStatus (
	var userName: String,
	var path: String,
	var systemTimeMillis: Long,
	var javaVersion: String
)