package at.ac.tuwien.big.we16.ue4.exception;

public class UserNotFoundException extends RequestException {
    @Override
    public int getCode() {
        return 404;
    }
}