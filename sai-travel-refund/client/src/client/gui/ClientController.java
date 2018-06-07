package client.gui;

import client.gateway.BrokerAppGateway;
import client.model.Address;
import client.model.ClientTravelMode;
import client.model.TravelRefundReply;
import client.model.TravelRefundRequest;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    @FXML
    private ComboBox cbTravelMode;
    @FXML
    private TextField tfCosts;
    @FXML
    private Label lbCosts;
    @FXML
    private ListView<ClientListLine> lvRequestReply;
    @FXML
    private TextField tfOriginStreet;
    @FXML
    private TextField tfOriginNumber;
    @FXML
    private TextField tfOriginCity;
    @FXML
    private TextField tfTeacher;
    @FXML
    private TextField tfDestinationStreet;
    @FXML
    private TextField tfDestinationNumber;
    @FXML
    private TextField tfDestinationCity;
    @FXML
    private TextField tfStudent;
    @FXML
    private Button btnSend;

    BrokerAppGateway brokerAppGateway;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbTravelMode.getItems().addAll(
                "car",
                "public transport"
        );
        cbTravelMode.getSelectionModel().select(0);
        jcbModeItemStateChanged();
        brokerAppGateway= new BrokerAppGateway() {
            @Override
            public void onLoanReplyArrived(TravelRefundReply travelRefundReply) {
                System.out.println("arrived");
            }
        };
    }

    private ClientListLine getRequestReply(TravelRefundRequest request) {

        for (int i = 0; i < lvRequestReply.getItems().size(); i++) {
            ClientListLine rr = lvRequestReply.getItems().get(i);
            if (rr.getRequest() == request) {
                return rr;
            }
        }

        return null;
    }
    @FXML
    private void jbSendActionPerformed() {
        requestMessage();
        System.out.println("raboti");
    }

    @FXML
    private void jcbModeItemStateChanged() {
        int mode = cbTravelMode.getSelectionModel().getSelectedIndex();
        int costs;
        if (mode == ClientTravelMode.PUBLIC_TRANSPORT.ordinal()){
            costs = 60;
            tfCosts.setEditable(true);
            tfCosts.setVisible(true);
            lbCosts.setVisible(true);
        } else {
            costs = 0;
            tfCosts.setEditable(false);
            tfCosts.setVisible(false);
            lbCosts.setVisible(false);
        }
        tfCosts.setText(Integer.toString(costs));
    }

    public void requestMessage()
    {
       String  orgStreet=tfOriginStreet.getText();
       String desStreet=tfDestinationStreet.getText();

       String orgCity=tfOriginCity.getText();
       String destCity=tfDestinationCity.getText();

       int orgNumber= Integer.parseInt(tfOriginNumber.getText());
        int destNumber= Integer.parseInt(tfDestinationNumber.getText());

        String teacher=tfTeacher.getText();
        String student=tfStudent.getText();

        double travelCost=0;
        if(cbTravelMode.getSelectionModel().getSelectedIndex()==1)
        {
            travelCost=Double.parseDouble(tfCosts.getText());
        }
        Address orgAddress=new Address(orgStreet,orgNumber,orgCity);
        Address destAddress=new Address(desStreet,destNumber,destCity);
        TravelRefundRequest travelRefundRequest=new TravelRefundRequest(teacher,student,orgAddress, destAddress,travelCost);
        System.out.println(travelRefundRequest.getCosts());
        System.out.println(travelRefundRequest.getStudent());
        System.out.println(travelRefundRequest.getMode());
        brokerAppGateway.applyForRefund(travelRefundRequest);
        ClientListLine clientListLine=new ClientListLine(travelRefundRequest);
        lvRequestReply.getItems().add(clientListLine);

    }
}
