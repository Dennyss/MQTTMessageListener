package consumer;

import common.MQTTChannel;
import common.MessageListener;
import properties.PropertyLoader;

/**
 * Created by Denys Kovalenko on 9/26/2014.
 */
public class MessageSubscriber implements MessageListener {

    public static void main(String args[]) throws Exception {
        if (args.length != 1) {
            System.out.println("The property file path is required as an application argument");
        }
        PropertyLoader.loadProperties(args[0]);

        MQTTChannel mqttChannel = new MQTTChannel();
        mqttChannel.connect();
        mqttChannel.subscribe();

        MessageSubscriber messageListener = new MessageSubscriber();
        mqttChannel.startListening(messageListener);

        System.out.println("Starting listening messages from MQTT channel .... \n");
    }


    @Override
    public void onMessage(String topicName, String payload) {
        System.out.print("Message received. Topic name: " + topicName + ". Payload: " + payload + ";\n");
    }
}
