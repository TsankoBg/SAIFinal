package approval.gui;

import approval.model.ApprovalReply;
import approval.model.ApprovalRequest;
import gateway.BrokerAppGateway;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class ApprovalController implements Initializable {

    @FXML
    private ListView<ApprovalListLine> lvRequestReply;
    @FXML
    private RadioButton rbApprove;
    @FXML
    private RadioButton rbReject;
    @FXML
    private Button btnSendReply;

    private String approvalName;

    BrokerAppGateway brokerAppGateway;

    public ApprovalController(String approvalName) {
        this.approvalName = approvalName;
        System.out.println(approvalName);
        brokerAppGateway =new BrokerAppGateway(approvalName) {
            @Override
            public void onApprovalReplyReceived(ApprovalRequest approvalRequest) {
                ApprovalListLine approvalListLine=new ApprovalListLine(approvalRequest);
                Platform.runLater(() -> {
                lvRequestReply.getItems().add(approvalListLine);
                });
            }
        };

    }

    private void sendApprovalReply() {
       // TO DO create and send ApprovalReply
       boolean approvedRejected=false;
        ApprovalReply approvalReply=null;


        String coreId="";
        ApprovalRequest approvalRequest=lvRequestReply.getSelectionModel().getSelectedItem().getRequest();
        for(Map.Entry m:brokerAppGateway.apprRequestMessage.entrySet()){
           if(m.getValue()==approvalRequest)
           {
               coreId=m.getKey().toString();
               System.out.println(m.getKey());
               break;
           }
        }
        if(rbApprove.isSelected())
        {
            approvedRejected=true;
            approvalReply=new ApprovalReply(approvedRejected,"" );
        }
        if(rbReject.isSelected())
        {
            approvedRejected=false;
            approvalReply=new ApprovalReply(approvedRejected,approvalName);
        }
        lvRequestReply.getSelectionModel().getSelectedItem().setReply(approvalReply);
        lvRequestReply.refresh();
        brokerAppGateway.sendReply(approvalReply,coreId);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ToggleGroup radioButtonsGroup = new ToggleGroup();
        rbApprove.setToggleGroup(radioButtonsGroup);
        rbReject.setToggleGroup(radioButtonsGroup);

        btnSendReply.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                sendApprovalReply();
            }
        });

    }
}
