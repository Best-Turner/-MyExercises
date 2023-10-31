package exception;

/**
 * An exception thrown when attempting to create a transaction with a duplicate identifier.
 * <p>
 * This exception occurs when an attempt is made to create a transaction with an identifier that
 * already exists in the database. It inherits from the standard Exception class.
 */
public class DuplicateTransactionIdException extends Exception {

    /**
     * Constructor for the exception with a given message.
     *
     * @param message The error message.
     */
    public DuplicateTransactionIdException(String message) {
        super(message);
    }
}