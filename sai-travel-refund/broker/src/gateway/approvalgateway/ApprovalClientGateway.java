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

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ApprovalClientGateway {

    private Receiver receiver;
    private Sender financeSender;
    private Sender internhipSender;

    private List<Sender> senders;
 //   HashMap
    List<String> aggregatorIDs;

    private ApprovalSerializer seriealizer;
    AIDProducer aidProducer;
    String FINANCIAL_DEPARMENT  = "#{amount} >= 50";
    Evaluator evaluator;


    public ApprovalClientGateway() {
        internhipSender = new Sender("brokerToAdmin");
        financeSender=new Sender("brokerToAdmin1");
        senders.add(internhipSender);
        senders.add(financeSender);
        aidProducer=new AIDProducer();
        receiver = new Receiver("adminToBroker");


        evaluator=new Evaluator();

    }

    public void sendApprovalRequest(ApprovalRequest approvalRequest,String corelation) {
        //serialize the request to string
        String approvalRequestString = seriealizer.requestToString(approvalRequest);
        //genderate Unique ID for aggragation property
        String aggregatorId= aidProducer.getUniqueID(aggregatorIDs);

        evaluator.putVariable("amount",Double.toString(approvalRequest.getCosts()));
        String result = null;
        try {
            result = evaluator.evaluate(FINANCIAL_DEPARMENT);
        } catch (EvaluationException e) {
            e.printStackTrace();
        }
        boolean finRule = result.equals("1.0");

        if(finRule)
        {
            financeSender.sendMessage(approvalRequestString,corelation,aggregatorId);
        }
        internhipSender.sendMessage(approvalRequestString,corelation,aggregatorId);

    }

    public void onLoanReplyArrived() {

        receiver.setMessageListener(message -> {

            ApprovalReply approvalReply = seriealizer.replyFromString(message.toString());

        });
    }
}
