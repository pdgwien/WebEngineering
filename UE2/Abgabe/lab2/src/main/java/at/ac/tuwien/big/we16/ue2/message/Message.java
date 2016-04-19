package at.ac.tuwien.big.we16.ue2.message;

/**
 * Created by Patrick on 19/04/16.
 */
public abstract class Message {
    protected String type;

    public Message(String type) {
        this.type = type;
    }
}
