package gui;

import gateway.Enricher.GoogleMatrixData;
import gateway.Enricher.PricePerKM;
import gateway.approvalgateway.ApprovalClientGateway;
import gateway.clientgateway.ClientAppGateway;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import model.ApprovalReply;
import model.ApprovalRequest;
import model.TravelRefundReply;
import model.TravelRefundRequest;


import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class BrokerController implements Initializable {


    ClientAppGateway clientAppGateway;
    ApprovalClientGateway approvalClientGateway;

    @FXML
    private ListView<BrorkerListLine> lvRequestReply;

    private BrorkerListLine getRequestReply(ApprovalRequest request) {
        for (int i = 0; i < lvRequestReply.getItems().size(); i++) {
            BrorkerListLine rr = lvRequestReply.getItems().get(i);
            if (rr.getRequest() == request) {
                return rr;
            }
        }
        return null;
    }

    private int getAggregatorCount(HashMap<String, String> map, HashMap<String, String> financeMap, String agID) {
        int counter1 = 0;
        for (Map.Entry m : map.entrySet()) {
            if (m.getKey().equals(agID)) {
                counter1++;
                System.out.println(counter1);
            }
        }
        for (Map.Entry m : financeMap.entrySet()) {
            if (m.getKey().equals(agID)) {
                counter1++;
                System.out.println(counter1);
            }
        }
        return counter1;
    }

    public BrokerController() {

        approvalClientGateway = new ApprovalClientGateway() {
            @Override
            public void onApprovalReply(ApprovalReply approvalReply, String messageCorelation, String agID) {
                ApprovalRequest request = null;
                for (Map.Entry m : appRequestCorelation.entrySet()) {
                    if (m.getKey().equals(messageCorelation)) {
                        try {
                            request = (ApprovalRequest) m.getValue();
                        } catch (Exception ex) {
                        }
                        break;
                    }
                }
                getRequestReply(request).setReply(approvalReply);
                Platform.runLater(() -> {
                    lvRequestReply.refresh();
                });
                if (getAggregatorCount(appAggrIDsFinance, appAggrIDsIntern, agID) == 0) {
                    TravelRefundReply travelRefundReply = new TravelRefundReply(approvalReply.isApproved(), approvalReply.getReasonRejected(), request.getCosts());
                    clientAppGateway.sendTravelFundReply(travelRefundReply, messageCorelation);
                }
            }
        };

        clientAppGateway = new ClientAppGateway() {
            @Override
            public void onLoanReplyArrived(TravelRefundRequest travelRefundRequest, String corelation) {
                ApprovalRequest approvalRequest = new ApprovalRequest(travelRefundRequest.getTeacher(), travelRefundRequest.getStudent(), travelRefundRequest.getCosts());
                approvalClientGateway.sendApprovalRequest(approvalRequest, corelation);
                BrorkerListLine brorkerListLine = new BrorkerListLine(approvalRequest);
                Platform.runLater(() -> {
                    lvRequestReply.getItems().add(brorkerListLine);
                });
            }
        };
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
