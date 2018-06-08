package gui;

import model.ApprovalReply;
import model.ApprovalRequest;
import model.TravelRefundReply;
import model.TravelRefundRequest;

public class BrorkerListLine {
    private ApprovalRequest request;
    private ApprovalReply reply;

    public BrorkerListLine(ApprovalRequest request, ApprovalReply reply) {
        this.request = request;
        this.reply = reply;
    }
    public BrorkerListLine(ApprovalRequest request) {
        this.request = request;
        // this.reply = reply;
    }
    public ApprovalRequest getRequest() {
        return request;
    }

    private void setRequest(ApprovalRequest request) {
        this.request = request;
    }

    public ApprovalReply getReply() {
        return reply;
    }

    public void setReply(ApprovalReply reply) {
        this.reply = reply;
    }

    @Override
    public String toString() {
        return request.toString() + "  --->  " + ((reply != null) ? reply.toString() : "waiting...");
    }
}
