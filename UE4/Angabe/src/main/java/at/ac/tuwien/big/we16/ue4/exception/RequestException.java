package at.ac.tuwien.big.we16.ue4.exception;

public abstract class RequestException extends Exception {
    public RequestException() {}

    public RequestException(String message) {
        super(message);
    }

    public abstract int getCode();
}
