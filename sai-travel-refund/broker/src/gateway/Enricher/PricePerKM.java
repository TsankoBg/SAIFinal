package gateway.Enricher;

import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class PricePerKM {

    ClientConfig config;
    Client client;
    URI baseURI;

    public PricePerKM() {
        config = new ClientConfig();
        client = ClientBuilder.newClient(config);
        baseURI = UriBuilder.fromUri("http://localhost:8080/priceperkm8/rest/price/").build();

    }

    public double getPricePerKM() {

        WebTarget serviceTarget = client.target(baseURI);
        WebTarget operationTarger = serviceTarget;
        Invocation.Builder requestBuilder = operationTarger.request().accept(MediaType.TEXT_PLAIN);
        Response response = requestBuilder.get();
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            String json = "";
            json = response.readEntity(String.class);
            double price = Double.parseDouble(json);
            return price;
        } else {
            return -1;
        }
    }
}

