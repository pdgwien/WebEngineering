package at.ac.tuwien.big.we16.ue4.service;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import at.ac.tuwien.big.we16.ue4.model.Bid;
import at.ac.tuwien.big.we16.ue4.model.Product;
import twitter4j.JSONException;
import twitter4j.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RESTService {

    private static final String HTTP = "https://lectures.ecosio.com/b3a/api/v1/bids";

    public String sendToRESTAPI(Product product, Bid bid) {
    	//Ignore ...
    	if (true) return product.getId();
        HttpURLConnection connection=null;
        RestResponse response = null;
        try {
            URL url = new URL(HTTP);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/json");
            connection.connect();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", bid.getUser().getFullName());
            jsonObject.put("product", product.getName_en());
            jsonObject.put("price", Float.toString(bid.getConvertedAmount()));
            jsonObject.put("date", product.getAuctionEnd().toString());
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            out.write(jsonObject.toString());
            out.close();

            int HttpResult =connection.getResponseCode();
            if(HttpResult ==HttpURLConnection.HTTP_OK){
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    response = loadResponse(line);
                }
                reader.close();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally{
            if(connection!=null)
                connection.disconnect();
        }
        return response.getId();
    }

    private RestResponse loadResponse(String line) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(line, RestResponse.class);
    }

    private class RestResponse {
        private String id;
        private String name;
        private String product;
        private String price;
        private String date;
        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getProduct() {
            return product;
        }
        public void setProduct(String product) {
            this.product = product;
        }
        public String getPrice() {
            return price;
        }
        public void setPrice(String price) {
            this.price = price;
        }
        public String getDate() {
            return date;
        }
        public void setDate(String date) {
            this.date = date;
        }
    }
}


