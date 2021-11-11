package net.corda.workbench.serviceBus


import com.typesafe.config.ConfigFactory
import net.corda.workbench.commons.registry.Registry
import net.corda.workbench.serviceBus.messaging.AzureConfig
import net.corda.workbench.serviceBus.messaging.Connection
import java.nio.charset.StandardCharsets


fun main(args: Array<String>) {
    val conf = ConfigFactory.load()
    val registry = Registry().store(AzureConfig(conf))
    val receiver = Connection(registry).egressReceiverClient()

    while(true){
        for (message in receiver.receiveMessages(1)) {
            val body = message.body
            val bodyText = String(body.toBytes(), StandardCharsets.UTF_8)
            println(bodyText)
            receiver.complete(message)
        }
    }

    //System.exit(0)  // need to force this for some reason
}
