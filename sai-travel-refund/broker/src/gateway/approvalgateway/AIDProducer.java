package gateway.approvalgateway;

import java.util.List;
import java.util.UUID;

public class AIDProducer {

    private String aggregatorID;

    public AIDProducer()
    {

    }

    public String getUniqueID(List<String> ids)
    {
        try {
            String aggregatorID = UUID.randomUUID().toString();

            while (ids.contains(aggregatorID)) {
                aggregatorID = UUID.randomUUID().toString();
            }
            System.out.println(aggregatorID);
            return aggregatorID;
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        return null;
    }

}
