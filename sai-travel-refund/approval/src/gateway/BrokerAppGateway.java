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
    public   HashMap<String, ApprovalRequest> apprAggregIDs;

    public BrokerAppGateway(String departmentName)
    {
        apprAggregIDs=new HashMap<>();
        apprRequestMessage=new HashMap<>();
        receiver=new Receiver(departmentName);
        sender=new Sender("adminToBroker");
        seriealizer=new ApprovalSerielizer();
        receiver.setMessageListener(message -> {
            onApprovalReplyReceived(message);


        });
    }

    public void sendReply(ApprovalReply approvalReply, String corelation, String agID, String type) {
        String request=seriealizer.replyToString(approvalReply);
        sender.sendMessage(request,corelation,agID,type);
    }

    public void onApprovalReplyReceived(Message message ) {

        try {
            ApprovalRequest approvalRequest=seriealizer.requestFromString(((TextMessage)message).getText());
            String agID= message.getStringProperty("aggregationID");
           // System.out.println(agID);
            apprRequestMessage.put(message.getJMSCorrelationID(),approvalRequest);
            apprAggregIDs.put(agID,approvalRequest);
            onApprovalReplyReceived(approvalRequest);
        } catch (JMSException e) {
            e.printStackTrace();
        }


    }
    public abstract void onApprovalReplyReceived(ApprovalRequest approvalRequest);
}
