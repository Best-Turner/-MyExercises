package exception;

public class DuplicateTransactionIdException extends Exception {
    public DuplicateTransactionIdException(String message) {
        super(message);
    }
}
