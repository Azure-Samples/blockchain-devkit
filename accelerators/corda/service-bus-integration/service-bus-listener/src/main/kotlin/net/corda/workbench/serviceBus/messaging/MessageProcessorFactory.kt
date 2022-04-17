package net.corda.workbench.serviceBus.messaging

import com.azure.messaging.servicebus.ServiceBusSenderClient
import com.azure.messaging.servicebus.ServiceBusReceivedMessage
import net.corda.workbench.commons.registry.Registry


class MessageProcessorFactory(private val registry: Registry, private val queueClient: ServiceBusSenderClient) {

    fun createProcessor(msg: ServiceBusReceivedMessage): IngressReceiveMessageProcessor {
        return IngressReceiveMessageProcessor(registry, msg, queueClient)
    }

}