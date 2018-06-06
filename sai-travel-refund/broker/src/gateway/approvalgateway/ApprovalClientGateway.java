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

public class ApprovalClientGateway {

    private Receiver receiver;
    private Sender sender;
    private ApprovalSerializer seriealizer;
    String FINANCIAL_DEPARMENT  = "#{amount} >= 50";
    Evaluator evaluator;
    public ApprovalClientGateway() {
        sender = new Sender("brokerToAdmin");
        receiver = new Receiver("adminToBroker");
        evaluator=new Evaluator();
    }

    public void sendApprovalRequest(ApprovalRequest approvalRequest,String corelation) {

        String approvalRequestString = seriealizer.requestToString(approvalRequest);
        sender.sendMessage(approvalRequestString,corelation);
        evaluator.putVariable("amount",Double.toString(approvalRequest.getCosts()));
        String result = null; // evaluate ING rule
        try {
            result = evaluator.evaluate(FINANCIAL_DEPARMENT);
        } catch (EvaluationException e) {
            e.printStackTrace();
        }
        boolean finRule = result.equals("1.0");



    }

    public void onLoanReplyArrived() {

        receiver.setMessageListener(message -> {

            ApprovalReply approvalReply = seriealizer.replyFromString(message.toString());

        });
    }
}
