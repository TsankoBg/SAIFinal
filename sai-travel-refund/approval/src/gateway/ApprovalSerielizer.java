package gateway;

import approval.model.ApprovalReply;
import approval.model.ApprovalRequest;
import com.owlike.genson.Genson;

public class ApprovalSerielizer {


    Genson genson;

    public ApprovalSerielizer() {
        genson = new Genson();
    }

    public String requestToString(ApprovalRequest approvalRequest) {
        String get = genson.serialize(approvalRequest);
        return get;
    }

    public ApprovalRequest requestFromString(String json) {
        ApprovalRequest travelRefundRequest = genson.deserialize(json, ApprovalRequest.class);
        return travelRefundRequest;
    }

    public String replyToString(ApprovalReply approvalReply) {
        String reply = genson.serialize(approvalReply);
        return reply;
    }

    public ApprovalReply replyFromString(String json)
    {
        ApprovalReply approvalReply= genson.deserialize(json, ApprovalReply.class);
        return approvalReply;
    }
}
