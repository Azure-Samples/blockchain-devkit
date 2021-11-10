package net.corda.workbench.serviceBus.messaging

import com.azure.core.amqp.AmqpRetryOptions
import com.azure.messaging.servicebus.models.ServiceBusReceiveMode
import com.azure.messaging.servicebus.ServiceBusClientBuilder
import com.azure.messaging.servicebus.ServiceBusReceiverAsyncClient
import com.azure.messaging.servicebus.ServiceBusSenderClient
import net.corda.workbench.commons.registry.Registry
import java.io.PrintStream
import java.time.Duration

/**
 * AzureConfig details for talking to a azure service bus
 */
class Connection(private val registry: Registry) {

    fun ingressQueueClient(): ServiceBusSenderClient {
        val config = registry.retrieve(AzureConfig::class.java)
        val sendClient = ServiceBusClientBuilder()
            .connectionString(config.endpoint)
            .retryOptions(AmqpRetryOptions()
                .setMaxDelay(Duration.ofSeconds(10))
                .setDelay(Duration.ofSeconds(1))
                .setMaxRetries(5))
            .sender()
            .queueName(config.ingressQueue)
            .buildClient()

        val ps = registry.retrieveOrElse(PrintStream::class.java, System.out)
        ps.println("Sender connected to queue ${config.ingressQueue} using endpoint ${config.endpoint}")
        return sendClient
    }

    fun egressQueueClient(): ServiceBusSenderClient {
        val config = registry.retrieve(AzureConfig::class.java)
        val sendClient = ServiceBusClientBuilder()
            .connectionString(config.endpoint)
            .sender()
            .queueName(config.egressQueue)
            .buildClient()

        val ps = registry.retrieveOrElse(PrintStream::class.java, System.out)
        ps.println("Sender connected to queue ${config.egressQueue} using endpoint ${config.endpoint}")
        return sendClient
    }

    fun ingressQueueReceiver(): ServiceBusReceiverAsyncClient {
        val config = registry.retrieve(AzureConfig::class.java)
        val receiver = ServiceBusClientBuilder()
            .connectionString(config.endpoint)
            .receiver()
            .receiveMode(ServiceBusReceiveMode.PEEK_LOCK)
            .queueName(config.ingressQueue)
            .buildAsyncClient()

        val ps = registry.retrieveOrElse(PrintStream::class.java, System.out)
        ps.println("Receiver connected to queue ${config.ingressQueue} using endpoint ${config.endpoint}")

        return receiver
    }

    fun egressQueueReceiver(): ServiceBusReceiverAsyncClient {
        val config = registry.retrieve(AzureConfig::class.java)
        val receiver = ServiceBusClientBuilder()
            .connectionString(config.endpoint)
            .receiver()
            .receiveMode(ServiceBusReceiveMode.PEEK_LOCK)
            .queueName(config.egressQueue)
            .buildAsyncClient()

        val ps = registry.retrieveOrElse(PrintStream::class.java, System.out)
        ps.println("Receiver connected to queue ${config.egressQueue} using endpoint ${config.endpoint}")

        return receiver
    }

}