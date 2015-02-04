package common;

import org.fusesource.mqtt.client.*;
import properties.PropertyKeys;
import properties.PropertyLoader;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.fusesource.hawtbuf.Buffer.utf8;

/**
 * Created by Denys Kovalenko on 9/29/2014.
 */
public class MQTTChannel implements MessageListener {
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private MQTT mqttFactory;
    private BlockingConnection connection;

    public MQTTChannel() throws Exception {
        String host = PropertyLoader.getProperty(PropertyKeys.HOST);
        int port = Integer.valueOf(PropertyLoader.getProperty(PropertyKeys.PORT));
        String user = PropertyLoader.getProperty(PropertyKeys.USERNAME);
        String password = PropertyLoader.getProperty(PropertyKeys.PASSWORD);

        mqttFactory = new MQTT();
        mqttFactory.setHost(host, port);
        mqttFactory.setClientId(createRandomString());
        mqttFactory.setCleanSession(true);

        if (isNotEmpty(user, password)) {
            mqttFactory.setUserName(user);
            mqttFactory.setPassword(password);
        }
    }

    public void connect() throws Exception {
        connection = mqttFactory.blockingConnection();
        connection.connect();
    }

    public void disconnect() throws Exception {
        if (connection != null) {
            connection.disconnect();
        }

        executorService.shutdown();
    }

    public void subscribe() throws Exception {
        String topic = PropertyLoader.getProperty(PropertyKeys.TOPIC);
        connection.subscribe(new Topic[]{new Topic(utf8(topic), QoS.EXACTLY_ONCE)});

        startListening(this);
    }

    public void startListening(final MessageListener listener) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Message message = connection.receive();
                        message.ack();
                        if (listener != null) {
                            String textMessage = message.getPayloadBuffer().utf8().toString();
                            // Shutdown case
                            if (textMessage.contains("shutdown")) {
                                System.out.println("Shutting down ... \n");
                                disconnect();
                                System.exit(0);
                                break;
                            }

                            listener.onMessage(message.getTopic(), textMessage);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onMessage(String topicName, String payload) {
        System.out.print("Message received. Topic name: " + topicName + ". Payload: " + payload + ";\n");
    }

    private String createRandomString() {
        Random random = new Random(System.currentTimeMillis());
        long randomLong = random.nextLong();
        return Long.toHexString(randomLong);
    }

    private boolean isNotEmpty(String user, String password) {
        if (user != null && !user.isEmpty() && password != null && !password.isEmpty()) {
            return true;
        }

        return false;
    }

}
