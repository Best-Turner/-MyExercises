package infrastructure;

import domain.model.Transaction;
import exception.DuplicateTransactionIdException;

import java.sql.SQLException;
import java.util.List;

/**
 * The TransactionRepository interface defines methods for interacting with data related to transactions.
 * This interface provides a contract for classes that will implement access and manipulation of data
 * about transactions in the system.
 *
 * @see Transaction
 * @see DuplicateTransactionIdException
 */
public interface TransactionRepository {

    /**
     * Saves a transaction.
     *
     * @param transaction The transaction to be saved.
     */
    void saveTransaction(Transaction transaction) throws SQLException;

    /**
     * Retrieves a transaction by its identifier.
     *
     * @param transactionCode The identifier of the transaction.
     * @return The transaction with the specified identifier, or null if not found.
     */
    Transaction getTransactionByTransactionCode(String transactionCode) throws SQLException;

    /**
     * Returns the count of saved transactions in the repository.
     *
     * @return The number of transactions in the repository.
     */
    int getCountTransaction() throws SQLException;

}
