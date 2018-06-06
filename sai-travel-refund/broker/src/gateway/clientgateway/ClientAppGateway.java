package gateway.clientgateway;

import gateway.Enricher.CostCalculator;
import gateway.Receiver;
import gateway.Sender;
import gateway.Serializers.TravelRefundSerializer;
import model.ClientTravelMode;
import model.TravelRefundReply;
import model.TravelRefundRequest;
import net.sourceforge.jeval.Evaluator;

public class ClientAppGateway {

    private Receiver receiver;
    private Sender sender;
    private TravelRefundSerializer seriealizer;
    private CostCalculator costCalculator;

    public ClientAppGateway() {
        sender = new Sender("brokerToClient1");
        receiver = new Receiver("clientToBroker1");
        costCalculator = new CostCalculator();
    }

    public void sendTravelFundReply(TravelRefundReply travelRefundReply, String corelation) {
        String request = seriealizer.replyToString(travelRefundReply);
        sender.sendMessage(request, corelation);
    }

    public void onLoanReplyArrived() {

        receiver.setMessageListener(message -> {

            TravelRefundRequest travelRefundRequest = seriealizer.requestFromString(message.toString());
            if (travelRefundRequest.getMode() == ClientTravelMode.CAR) {
                double newCosts = costCalculator.calculateCosts(travelRefundRequest.getOrigin().getCity(), travelRefundRequest.getDestination().getCity());
                travelRefundRequest.setCosts(newCosts);
            }

        });
    }
}
