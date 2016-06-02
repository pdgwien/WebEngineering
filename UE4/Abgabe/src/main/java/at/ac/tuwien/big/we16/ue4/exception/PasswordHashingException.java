package at.ac.tuwien.big.we16.ue4.exception;

public class PasswordHashingException extends RequestException {
    public PasswordHashingException() {}

    public PasswordHashingException(String message) {
        super(message);
    }

    @Override
    public int getCode() {
        return 500;
    }
}
