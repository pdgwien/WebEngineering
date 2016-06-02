package at.ac.tuwien.big.we16.ue4.service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Wraps a status message to be pushed to Twitter
 * @author pl
 *
 */
public class TwitterStatusMessage {

    private String from;
    private String uuid;
    private Date dateTime;

    public TwitterStatusMessage(String from, String uuid, Date dateTime) {
        if (from == null)
            throw new IllegalArgumentException("From must not be null.");

        if (uuid == null)
            throw new IllegalArgumentException("UUID must not be null");

        if (dateTime == null)
            throw new IllegalArgumentException("DateTime must not be null");

        this.from = from;
        this.uuid = uuid;
        this.dateTime = dateTime;
    }

    /**
     * Return the string to be published on Twitter
     */
    public String getTwitterPublicationString() {
        StringBuilder sb = new StringBuilder();
        sb.append((new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ")).format(dateTime)).append(": ");
        sb.append("User ").append(from).append(" publizierte folgende UUID: ");
        sb.append(uuid);
        return sb.toString().trim();
    }

}
