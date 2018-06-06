package gateway.Enricher;

public class CostCalculator {

    GoogleMatrixData googleMatrixData;
    PricePerKM pricePerKM;


    public  CostCalculator()
    {
        googleMatrixData=new GoogleMatrixData();
        pricePerKM =new PricePerKM();
    }

    public double  calculateCosts(String origin, String destination)
    {
       double price= pricePerKM.getPricePerKM();
      double distance=  googleMatrixData.getDistance(origin,destination);
      double finalPrice=price*distance;
        return finalPrice ;
    }
}
