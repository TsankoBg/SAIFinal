package gateway;

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


    public  Sender (String channelName)
    {
        try {
            Properties service_properties = new Properties();
            service_properties.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                    "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            service_properties.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
            service_properties.put(("queue."+channelName), channelName);
            InitialContext jndiContextC1 = new InitialContext(service_properties);
            ConnectionFactory connectionFactory = (ConnectionFactory) jndiContextC1
                    .lookup("ConnectionFactory");
            javax.jms.Connection connection = connectionFactory.createConnection();

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            destination = (Destination) jndiContextC1.lookup(channelName);
            producer = session.createProducer(destination);
            connection.start();


        } catch (NamingException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String  body, String corelId, String agregID,String type) {
        try {
            TextMessage message = session.createTextMessage();
           message.setText(body);
            message.setJMSReplyTo(destination);
            message.setStringProperty("type",type);
            message.setJMSCorrelationID(corelId);
            message.setStringProperty("aggregationID",agregID);
            producer.send(message);

        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
