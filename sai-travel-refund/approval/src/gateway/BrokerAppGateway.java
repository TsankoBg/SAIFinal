package gateway;

import approval.model.ApprovalReply;
import approval.model.ApprovalRequest;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.util.HashMap;

public abstract class BrokerAppGateway {

    private Receiver receiver;
    private Sender sender;
    private ApprovalSerielizer seriealizer;
  public   HashMap<String, ApprovalRequest> apprRequestMessage;


    public BrokerAppGateway(String departmentName)
    {
        apprRequestMessage=new HashMap<>();
        receiver=new Receiver(departmentName);
        sender=new Sender("adminToBroker");
        seriealizer=new ApprovalSerielizer();
        receiver.setMessageListener(message -> {
            onApprovalReplyReceived(message);


        });
    }

    public void sendReply(ApprovalReply approvalReply, String corelation) {
        String request=seriealizer.replyToString(approvalReply);
        sender.sendMessage(request,corelation);
    }

    public void onApprovalReplyReceived(Message message ) {

        try {
            ApprovalRequest approvalRequest=seriealizer.requestFromString(((TextMessage)message).getText());
            apprRequestMessage.put(message.getJMSCorrelationID(),approvalRequest);
            onApprovalReplyReceived(approvalRequest);
        } catch (JMSException e) {
            e.printStackTrace();
        }


    }
    public abstract void onApprovalReplyReceived(ApprovalRequest approvalRequest);
}
