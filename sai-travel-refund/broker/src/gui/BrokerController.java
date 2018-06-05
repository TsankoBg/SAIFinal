package gui;

import gateway.Enricher.GoogleMatrixData;
import gateway.Enricher.PricePerKM;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.Initializable;
import org.json.JSONException;

public class BrokerController implements Initializable {


    GoogleMatrixData googleMatrixData;
    PricePerKM pricePerKM;



    public BrokerController()
    {

        System.out.println("dada");
        googleMatrixData =new GoogleMatrixData();
        pricePerKM=new PricePerKM();
     //   Platform.runLater(() -> {

        try {
            googleMatrixData.getDistance("Eindhoven", "Amsterdam");
            System.out.println(pricePerKM.getPricePerKM() * googleMatrixData.getDistance("Eindhoven", "Sofia"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //   });
    }
    @Override
    public Initializable preInitialize() {
        return null;
    }

    @Override
    public ClientConfig getConfiguration() {
        return null;
    }
}
