package gui;

import gateway.Enricher.GoogleMatrixData;
import gateway.Enricher.PricePerKM;
import javafx.fxml.Initializable;


import java.net.URL;
import java.util.ResourceBundle;

public class BrokerController implements Initializable {


    GoogleMatrixData googleMatrixData;
    PricePerKM pricePerKM;



    public BrokerController()
    {

        System.out.println("dada");
        googleMatrixData =new GoogleMatrixData();
        pricePerKM=new PricePerKM();
     //   Platform.runLater(() -> {


            googleMatrixData.getDistance("Eindhoven", "Amsterdam");
            System.out.println(pricePerKM.getPricePerKM() * googleMatrixData.getDistance("Eindhoven", "Sofia"));

        //   });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
