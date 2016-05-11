package at.ac.tuwien.big.we16.ue3.service;

import at.ac.tuwien.big.we16.ue3.model.Product;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by David on 11.05.16.
 */
public class BoardService {

    private static final Logger logger = LogManager.getLogger(BoardService.class);

    public void postSale(Product product) {

        logger.info("Posting sold product");
        try {

            SalePojo pojo = new SalePojo(product);

            String postUrl = "https://lectures.ecosio.com/b3a/api/v1/bids";
            Gson gson = new Gson();
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost post = new HttpPost(postUrl);
            StringEntity postingString = new StringEntity(gson.toJson(pojo));
            post.setEntity(postingString);
            post.setHeader("Content-type", "application/json");
            HttpResponse response = httpClient.execute(post);
            String json = EntityUtils.toString(response.getEntity());

            if (response.getStatusLine().getStatusCode()==200){
                ResponsePojo boardResponse = gson.fromJson(json, ResponsePojo.class);

                tweetUUID(boardResponse);
            }

        } catch(IOException e){ //TODO Fehler Separat abfangen

            e.printStackTrace();

        }
    }

    private void tweetUUID (ResponsePojo boardResponse) {
        try {

            Date date = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(boardResponse.date));
            TwitterStatusMessage tsm = new TwitterStatusMessage(boardResponse.name, boardResponse.id, date);

            String consumerKey = "GZ6tiy1XyB9W0P4xEJudQ";
            String consumerSecret = "gaJDlW0vf7en46JwHAOkZsTHvtAiZ3QUd2mD1x26J9w";
            String accessToken = "1366513208-MutXEbBMAVOwrbFmZtj1r4Ih2vcoHGHE2207002";
            String accessSecret = "RMPWOePlus3xtURWRVnv1TgrjTyK7Zk33evp4KKyA";

            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
                    .setOAuthConsumerKey(consumerKey)
                    .setOAuthConsumerSecret(consumerSecret)
                    .setOAuthAccessToken(accessToken)
                    .setOAuthAccessTokenSecret(accessSecret);

            TwitterFactory factory = new TwitterFactory(cb.build());
            Twitter twitter = factory.getInstance();

            Status status = twitter.updateStatus(tsm.getTwitterPublicationString());
            logger.info("Tweeted following message: " + status.getText());


        } catch(ParseException e){
            logger.error("Bad date format from server!");
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    private class ResponsePojo{
        String name;
        String product;
        String price;
        String date;
        String id;
    }

    private class SalePojo {
        String name;
        String product;
        String price;
        String date;

        public SalePojo (Product product){

            SimpleDateFormat sdf = new SimpleDateFormat(
                    "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date now = new Date();

            date = sdf.format(now);
            name = product.getHighestBid().getUser().getFullName();
            this.product = product.getName();
            price = product.getHighestBid().getConvertedAmount() +"";
        }
    }
}
