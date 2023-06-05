# MQTT Spring Integration

## Specification

* Kotlin 1.7.22
* JDK 11
* Spring 2.7.5

## Message Brokers

Moquette - Embedded MQTT Message Broker. [GitHub](https://github.com/moquette-io/moquette)


## Project Structure

Modules:
* Client - contains CLI application that sends messages to server
* Model - contains common classes for Client and Server
* Server - contains Spring Boot Application that starts embedded MQTT Broker and handling messages
