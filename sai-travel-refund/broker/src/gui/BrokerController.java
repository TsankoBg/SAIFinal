package gui;

import gateway.Enricher.GoogleMatrixData;
import gateway.Enricher.PricePerKM;
import gateway.clientgateway.ClientAppGateway;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import model.TravelRefundRequest;


import java.net.URL;
import java.util.ResourceBundle;

public class BrokerController implements Initializable {


   ClientAppGateway clientAppGateway;

    @FXML
    private ListView<BrorkerListLine> lvRequestReply;

    private BrorkerListLine getRequestReply(TravelRefundRequest request) {

        for (int i = 0; i < lvRequestReply.getItems().size(); i++) {
            BrorkerListLine rr = lvRequestReply.getItems().get(i);
            if (rr.getRequest() == request) {
                return rr;
            }
        }

        return null;
    }
    public BrokerController()
    {
    clientAppGateway=new ClientAppGateway() {
        @Override
        public void onLoanReplyArrived(TravelRefundRequest travelRefundRequest) {
            lvRequestReply.getItems().add(getRequestReply(travelRefundRequest));
            System.out.println("v controller");

        }
    };

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
