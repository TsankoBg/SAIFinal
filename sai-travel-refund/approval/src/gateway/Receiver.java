package gateway;

import org.apache.activemq.broker.Connection;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class Receiver {


    Connection connection;
    Session session;
    Destination destination;
    MessageConsumer consumer;

    public Receiver(String channelName) {
        try {
            Properties service_properties = new Properties();
            service_properties.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                    "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            service_properties.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
            ;
            service_properties.put(("queue." + channelName), channelName);
            InitialContext jndiContextC1 = new InitialContext(service_properties);
            ConnectionFactory connectionFactory = (ConnectionFactory) jndiContextC1
                    .lookup("ConnectionFactory");
            javax.jms.Connection connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            destination = (Destination) jndiContextC1.lookup(channelName);
            consumer = session.createConsumer(destination);
            connection.start();

        } catch (NamingException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void setMessageListener(MessageListener ml) {
        try {
            consumer.setMessageListener(ml);

        } catch (JMSException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
