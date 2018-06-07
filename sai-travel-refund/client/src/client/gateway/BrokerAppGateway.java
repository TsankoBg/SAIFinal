package client.gateway;


import client.model.TravelRefundReply;
import client.model.TravelRefundRequest;

import java.util.HashMap;

public abstract class BrokerAppGateway {

    private Receiver receiver;
    private Sender sender;
    private ClientSeriealizer seriealizer;


public  BrokerAppGateway()
{
    receiver=new Receiver("brokerToClient1");
    sender=new Sender("clientToBroker1");
    seriealizer=new ClientSeriealizer();
}

    public void applyForRefund(TravelRefundRequest travelRefundRequest) {
    String request=seriealizer.requestToString(travelRefundRequest);
        sender.sendMessage(request);
    }

    public void onReplyArrived( ) {

        receiver.setMessageListener(message -> {

            TravelRefundReply travelRefundReply =seriealizer.replyFromString(message.toString());
            onLoanReplyArrived(travelRefundReply);
        });


    }
    public abstract  void onLoanReplyArrived(TravelRefundReply travelRefundReply);
}
