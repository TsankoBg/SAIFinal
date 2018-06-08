package client.gateway;


import client.model.TravelRefundReply;
import client.model.TravelRefundRequest;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.util.HashMap;
import java.util.UUID;

public abstract class BrokerAppGateway {

    private Receiver receiver;
    private Sender sender;
    private ClientSeriealizer seriealizer;
    public HashMap<String,TravelRefundRequest> travelRequestIDmap;


    public BrokerAppGateway() {
        travelRequestIDmap=new HashMap<>();
        receiver = new Receiver("brokerToClient1");
        sender = new Sender("clientToBroker1");
        seriealizer = new ClientSeriealizer();
        receiver.setMessageListener(message -> {
            onBrokerReplyArrived(message);
        });
    }

    public void applyForRefund(TravelRefundRequest travelRefundRequest) {
        String request = seriealizer.requestToString(travelRefundRequest);
        String corId= UUID.randomUUID().toString();
        sender.sendMessage(request,corId);
        travelRequestIDmap.put(corId,travelRefundRequest);
    }

    public void onBrokerReplyArrived(Message message) {
        TravelRefundReply travelRefundReply = null;
        try {
            travelRefundReply = seriealizer.replyFromString(((TextMessage) message).getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
        try {
            onBrokerReplyArrived(travelRefundReply, message.getJMSCorrelationID());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public abstract void onBrokerReplyArrived(TravelRefundReply travelFundReply, String corelationID);
}
