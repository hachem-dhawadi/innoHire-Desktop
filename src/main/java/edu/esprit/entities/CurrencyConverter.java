package edu.esprit.entities;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class CurrencyConverter {

    public static double convertToEUR(double amountInTND) {
        // Replace "TND" and "EUR" with the currency codes you want to convert from and to
        String fromCurrency = "TND";
        String toCurrency = "EUR";

        // Construct the API URL
        String apiUrl = "https://api.exchangerate-api.com/v4/latest/" + fromCurrency;

        try {
            // Create an HttpClient
            HttpClient httpClient = HttpClientBuilder.create().build();

            // Create an HTTP GET request
            HttpGet httpGet = new HttpGet(apiUrl);

            // Execute the request
            HttpResponse response = httpClient.execute(httpGet);

            // Parse the JSON response
            String jsonResponseString = EntityUtils.toString(response.getEntity());
            JSONObject jsonResponse = new JSONObject(jsonResponseString);

            // Check if the "rates" field is present in the response
            if (jsonResponse.has("rates")) {
                // Get the exchange rate
                double exchangeRate = jsonResponse.getJSONObject("rates").getDouble(toCurrency);

                // Calculate the converted amount
                double amountInEUR = amountInTND * exchangeRate;

                // Return the converted amount
                return amountInEUR;
            } else {
                System.out.println("Error: 'rates' field not found in the JSON response");
                // You might want to throw an exception or handle this case based on your requirements
                return -1; // Or another appropriate value indicating an error
            }
        } catch (Exception e) {
            e.printStackTrace();
            // You might want to throw an exception or handle this case based on your requirements
            return -1; // Or another appropriate value indicating an error
        }
    }

}
