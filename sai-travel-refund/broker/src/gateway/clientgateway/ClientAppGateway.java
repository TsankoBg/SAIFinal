package gateway.clientgateway;

import gateway.Enricher.CostCalculator;
import gateway.Receiver;
import gateway.Sender;
import gateway.Serializers.TravelRefundSerializer;
import model.ClientTravelMode;
import model.TravelRefundReply;
import model.TravelRefundRequest;
import net.sourceforge.jeval.Evaluator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.util.HashMap;

public abstract class ClientAppGateway {

    private Receiver receiver;
    private Sender sender;
    private TravelRefundSerializer seriealizer;
    private CostCalculator costCalculator;
   public HashMap<String, TravelRefundRequest> travelCorrelIDs;

    public ClientAppGateway() {
        sender = new Sender("brokerToClient1");
        receiver = new Receiver("clientToBroker1");
        costCalculator = new CostCalculator();
        seriealizer=new TravelRefundSerializer();
        receiver.setMessageListener(message -> {
            onLoanReplyArrived(message);
        });

    }


    public void sendTravelFundReply(TravelRefundReply travelRefundReply, String corelation) {
        String request = seriealizer.replyToString(travelRefundReply);
        sender.sendMessage(request, corelation, "");
    }

    public void onLoanReplyArrived(Message message) {



        try {
            TravelRefundRequest travelRefundRequest = seriealizer.requestFromString(((TextMessage)message).getText());

            if (travelRefundRequest.getMode() == ClientTravelMode.CAR) {
                double newCosts = costCalculator.calculateCosts(travelRefundRequest.getOrigin().getCity(), travelRefundRequest.getDestination().getCity());
                travelRefundRequest.setCosts(newCosts);
            }
            onLoanReplyArrived(travelRefundRequest, message.getJMSCorrelationID());
        } catch (JMSException e) {
            e.printStackTrace();
        }


    }

    public abstract void onLoanReplyArrived(TravelRefundRequest travelRefundRequest, String msgID);
}
