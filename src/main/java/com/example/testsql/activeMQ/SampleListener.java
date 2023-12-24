package com.example.testsql.activeMQ;

import com.example.testsql.models.User;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

public class SampleListener implements MessageListener {

    public void onMessage(Message message) {
        if (message instanceof ObjectMessage) {
            try {
                Object object = ((ObjectMessage) message).getObject();

                System.out.println(this.getClass().getName()
                        + "has received a message : " + ((User) object).getFirstName());
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
