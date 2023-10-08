package infrastructure;

import domain.model.Transaction;
import exception.DuplicateTransactionIdException;

public interface TransactionRepository {
    void saveTransaction(Transaction transaction) throws DuplicateTransactionIdException;
}
