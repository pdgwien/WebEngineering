package at.ac.tuwien.big.we16.ue3.exception;

public class UserNotFoundException extends RequestException {
    @Override
    public int getCode() {
        return 404;
    }
}