package gateway.approvalgateway;

import gateway.Receiver;
import gateway.Sender;
import gateway.Serializers.ApprovalSerializer;
import gateway.Serializers.TravelRefundSerializer;
import model.ApprovalReply;
import model.ApprovalRequest;
import model.TravelRefundReply;
import model.TravelRefundRequest;

public class ApprovalClientGateway {

    private Receiver receiver;
    private Sender sender;
    private ApprovalSerializer seriealizer;

    public ApprovalClientGateway() {
        sender = new Sender("brokerToAdmin");
        receiver = new Receiver("adminToBroker");
    }

    public void sendApprovalRequest(ApprovalRequest approvalRequest,String corelation) {

        String request = seriealizer.requestToString(approvalRequest);
        sender.sendMessage(request,corelation);
    }

    public void onLoanReplyArrived() {

        receiver.setMessageListener(message -> {

            ApprovalReply approvalReply = seriealizer.replyFromString(message.toString());

        });
    }
}
