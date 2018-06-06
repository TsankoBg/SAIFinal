package gateway;

import approval.model.ApprovalReply;
import approval.model.ApprovalRequest;

import javax.jms.Message;

public class BrokerAppGateway {

    private Receiver receiver;
    private Sender sender;
    private ApprovalSerielizer seriealizer;


    public BrokerAppGateway()
    {
        receiver=new Receiver("brokerToBank1");
        sender=new Sender("bankToBroker");
        seriealizer=new ApprovalSerielizer();
    }

    public void sendReply(ApprovalReply approvalReply, String corelation) {
        String request=seriealizer.replyToString(approvalReply);
        sender.sendMessage(request,corelation);
    }

    public void onLoanReplyArrived( ) {

        receiver.setMessageListener(message -> {

            ApprovalRequest approvalRequest =seriealizer.requestFromString(message.toString());

        });


    }
}
