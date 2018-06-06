package client.gateway;

import client.model.TravelRefundRequest;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class Sender {

    Connection connection;
    Session session;
    Destination destination;
    MessageProducer producer;

    public  Sender(String channelName)
    {
        try {
            Properties service_properties = new Properties();
            service_properties.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                    "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            service_properties.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
            service_properties.put(("queue."+ channelName), channelName);
            InitialContext jndiContextC1 = new InitialContext(service_properties);
            ConnectionFactory connectionFactory = (ConnectionFactory) jndiContextC1
                    .lookup("ConnectionFactory");
            javax.jms.Connection connection = connectionFactory.createConnection();

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            destination= (Destination) jndiContextC1.lookup(channelName);
            producer=session.createProducer(null);

            //  connection.start();

        } catch (NamingException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }


    public void sendMessage(String body)
    {
        try {
            TextMessage message = session.createTextMessage();
            message.setText(body);
            producer.send(destination,message);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
