package gateway.approvalgateway;

import gateway.Receiver;
import gateway.Sender;
import gateway.Serializers.ApprovalSerializer;
import gateway.Serializers.TravelRefundSerializer;
import model.ApprovalReply;
import model.ApprovalRequest;
import model.TravelRefundReply;
import model.TravelRefundRequest;
import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public abstract class ApprovalClientGateway {

    private Receiver receiver;
    private Sender financeSender;
    private Sender internhipSender;
    public HashMap<String,ApprovalRequest> appRequestCorelation;
    private List<Sender> senders;
    List<String> aggregatorIDs;

    private ApprovalSerializer seriealizer;
    AIDProducer aidProducer;
    String FINANCIAL_DEPARMENT  = "#{amount} >= 50";
    Evaluator evaluator;


    public ApprovalClientGateway()
    {
        //initialzing
        appRequestCorelation=new HashMap<>();
        senders=new ArrayList<>();
        internhipSender = new Sender("Internship Administration");
       financeSender=new Sender("Financial Department");
       aggregatorIDs=new ArrayList<>();
       senders.add(internhipSender);
      senders.add(financeSender);
        aidProducer=new AIDProducer();
        seriealizer=new ApprovalSerializer();
        receiver = new Receiver("adminToBroker");
        evaluator=new Evaluator();

        // on receiving message
        receiver.setMessageListener(message -> {

            onApprovalReply(message);

        });


    }

    public void sendApprovalRequest(ApprovalRequest approvalRequest,String messageID) {

        //serialize the request to string
        String approvalRequestString = seriealizer.requestToString(approvalRequest);
        //genderate Unique ID for aggragation property
        String aggregatorId=aidProducer.getUniqueID(aggregatorIDs);

        evaluator.putVariable("amount",Double.toString(approvalRequest.getCosts()));
        String result = null;
        try {
            result = evaluator.evaluate(FINANCIAL_DEPARMENT);
        } catch (EvaluationException e) {
            e.printStackTrace();
        }
        boolean finRule = result.equals("1.0");
        System.out.println(aggregatorId);
        if(finRule)
        {
           financeSender.sendMessage(approvalRequestString,messageID,aggregatorId);
        }
        internhipSender.sendMessage(approvalRequestString,messageID,aggregatorId);
        appRequestCorelation.put(messageID,approvalRequest);

    }

    public void onApprovalReply(Message message) {


        ApprovalReply approvalReply = null;
        try {
            approvalReply = seriealizer.replyFromString(((TextMessage)message).getText());
            onApprovalReply(approvalReply, message.getJMSCorrelationID());
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
    public abstract void onApprovalReply(ApprovalReply approvalReply, String messageCorelation );
}


