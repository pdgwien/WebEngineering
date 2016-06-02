package at.ac.tuwien.big.we16.ue4.service;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterService {
    private static final String consumerKey = "";
    private static final String consumerSecret = "";
    private static final String accessToken = "";
    private static final String accessSecret = "";

    public void publishUuid(TwitterStatusMessage message) throws TwitterException {
        ConfigurationBuilder cb = new ConfigurationBuilder();

        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerSecret)
                .setOAuthAccessToken(accessToken)
                .setOAuthAccessTokenSecret(accessSecret);
        TwitterFactory factory = new TwitterFactory(cb.build());
        Twitter twitter = factory.getInstance();
        //Disabled because not necessary here
        //Status status = twitter.updateStatus(message.getTwitterPublicationString());
    }
}