package at.ac.tuwien.big.we16.ue2.message;

/**
 * Created by patrickgrosslicht on 19/04/16.
 */
public class BalanceMessage extends Message {
    private long balance;

    public BalanceMessage(long balance) {
        super("balance");
        this.balance = balance;
    }
}
