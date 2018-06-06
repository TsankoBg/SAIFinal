package gateway.Enricher;

import org.glassfish.jersey.client.ClientConfig;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

import static javafx.scene.input.KeyCode.J;


public class GoogleMatrixData {


    ClientConfig config;
    Client client;
    URI baseURI;

   public GoogleMatrixData()
   {
       config = new ClientConfig();
       client = ClientBuilder.newClient(config);

   }
   public  double getDistance(String origin, String destination)  {
       baseURI = UriBuilder.fromUri("https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins="+ origin + "&destinations="+ destination+ "&language=en-FR&key=AIzaSyCy0S3UrSFvcCzXzJyyBOVCxWF1RiIX5MQ").build();
       WebTarget serviceTarget = client.target(baseURI);
       WebTarget operationTarger = serviceTarget;
       Invocation.Builder requestBuilder = operationTarger.request().accept(MediaType.TEXT_PLAIN);
       Response response = requestBuilder.get();
       if (response.getStatus() == Response.Status.OK.getStatusCode()) {
           try {
           String json="";
           json=response.readEntity(String.class);
           JSONObject jsonobj= null;

               jsonobj = new JSONObject(json);

           JSONArray dist=(JSONArray)jsonobj.get("rows");
           JSONObject obj2 = (JSONObject)dist.get(0);
           JSONArray disting=(JSONArray)obj2.get("elements");
           JSONObject obj3 = (JSONObject)disting.get(0);
           JSONObject obj4=(JSONObject)obj3.get("distance");
           JSONObject obj5=(JSONObject)obj3.get("duration");



           String distanceString=obj4.get("text").toString().replaceAll("\\D+","");
         //  ASCII_DIGITS.retainFrom("123-456-789");
           double distance=Double.parseDouble(distanceString);
           return distance ;
           } catch (JSONException e) {
               e.printStackTrace();
           }
         return -1;

       } else {
           System.err.println(" Error from GoogleMatrix Response");
           return -1 ;
       }
   }

}
