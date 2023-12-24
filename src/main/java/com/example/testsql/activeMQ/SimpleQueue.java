package com.example.testsql.activeMQ;

import static jakarta.jms.Session.AUTO_ACKNOWLEDGE;

import jakarta.jms.*;

import org.apache.activemq.ActiveMQConnectionFactory;

public class SimpleQueue {

    private static final String CLIENTID = "TrishInfotechActiveMQ";
    private String queueName;
    private ActiveMQConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;
    private Destination destination;
    private MessageProducer producer;
    private MessageConsumer consumer;

    public SimpleQueue(String queueName) throws Exception {
        super();
        this.queueName = queueName;
        connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        connection = connectionFactory.createConnection();
        connection.setClientID(CLIENTID);
        connection.start();
        session = connection.createSession(false, AUTO_ACKNOWLEDGE);
        destination = session.createQueue(this.queueName);
        producer = session.createProducer(destination);
        consumer = session.createConsumer(destination);
    }

    public void send(byte[] fileBytes) throws Exception {
        BytesMessage message = session.createBytesMessage();

        message.writeBytes(fileBytes);

        producer.send(message);

        System.out.println("Le fichier a été envoyé avec succès à la file d'attente."+message);
    }

    public byte[] receive() {
        try {
            Message message = consumer.receive();

            if (message instanceof BytesMessage) {
                BytesMessage bytesMessage = (BytesMessage) message;

                byte[] fileBytes = new byte[(int) bytesMessage.getBodyLength()];
                bytesMessage.readBytes(fileBytes);

                System.out.println("Received file with " + fileBytes.length + " bytes.");

                return fileBytes;
            } else {
                System.out.println("Received a message that is not a BytesMessage.");
            }
        } catch (Exception e) {
            e.printStackTrace(); // Gérer les erreurs correctement dans un environnement de production
        }

        return null;
    }


    public void close() throws JMSException {
        producer.close();
        producer = null;
        consumer.close();
        consumer = null;
        session.close();
        session = null;
        connection.close();
        connection = null;
    }
}
