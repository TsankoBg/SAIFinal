package client.gateway;

import client.model.TravelRefundReply;
import client.model.TravelRefundRequest;
import com.owlike.genson.Genson;

public class ClientSeriealizer {

    Genson genson;


    public ClientSeriealizer() {
        genson = new Genson();
    }

    public String requestToString(TravelRefundRequest travelRefundRequest) {
        String get = genson.serialize(travelRefundRequest);
        return get;
    }

    public TravelRefundRequest requestFromString(String json) {
        TravelRefundRequest travelRefundRequest = genson.deserialize(json, TravelRefundRequest.class);
        return travelRefundRequest;
    }

    public String replyToString(TravelRefundReply travelRefundReply) {
        String reply = genson.serialize(travelRefundReply);
        return reply;
    }

    public TravelRefundReply replyFromString(String json)
    {
        TravelRefundReply travelRefundReply= genson.deserialize(json, TravelRefundReply.class);
        return travelRefundReply;
    }
}
