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
import java.util.*;

public abstract class ApprovalClientGateway {

    private Receiver receiver;
    private Sender financeSender;
    private Sender internhipSender;
    public HashMap<String,ApprovalRequest> appRequestCorelation;
    public HashMap<String,ApprovalReply> internReply;
    public  HashMap<String, ApprovalReply> financeReply;

 //   private List<Sender> senders;
    List<String> aggregatorIDs;
    public HashMap<String, String> appAggrIDsFinance;
    public HashMap<String, String> appAggrIDsIntern;
    private ApprovalSerializer seriealizer;
    AIDProducer aidProducer;
    String FINANCIAL_DEPARMENT  = "#{amount} >= 50";
    Evaluator evaluator;


    public ApprovalClientGateway()
    {
        //initialzing
        appRequestCorelation=new HashMap<>();
        appAggrIDsFinance=new HashMap<>();
        appAggrIDsIntern=new HashMap<>();
        internReply=new HashMap<>();
        financeReply=new HashMap<>();
       // senders=new ArrayList<>();
        internhipSender = new Sender("Internship Administration");
       financeSender=new Sender("Financial Department");
       aggregatorIDs=new ArrayList<>();

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
        System.out.println( "BROKER SENT AGGREGATION ID : "+ aggregatorId);
        if(finRule)
        {
           financeSender.sendMessage(approvalRequestString,messageID,aggregatorId);

            appAggrIDsFinance.put(aggregatorId,"Finance");
        }
        internhipSender.sendMessage(approvalRequestString,messageID,aggregatorId);

        appRequestCorelation.put(messageID,approvalRequest);
        appAggrIDsIntern.put(aggregatorId,"Intern");

    }

    public void onApprovalReply(Message message) {



        try {
         ApprovalReply   approvalReply = seriealizer.replyFromString(((TextMessage)message).getText());

         String agregID=message.getStringProperty("aggregationID");
       //  System.out.println(approvalReply.getReasonRejected());

    if(message.getStringProperty("type").equals("Financial Department"))
    {
        appAggrIDsFinance.remove(agregID,"Finance");
        System.out.println("Finance with id "+ agregID  + " was removed");
        financeReply.put(message.getJMSCorrelationID(),approvalReply);

    }
    if(message.getStringProperty("type").equals("Internship Administration"))
    {
        appAggrIDsIntern.remove(agregID,"Intern");
        System.out.println("intern with id "+ agregID  + " was removed");
        internReply.put(message.getJMSCorrelationID(),approvalReply);
        }
            onApprovalReply(approvalReply, message.getJMSCorrelationID(),agregID);
           // appAggrIDs.remove(agregID);
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
    public abstract void onApprovalReply(ApprovalReply approvalReply, String messageCorelation, String agID );
}


