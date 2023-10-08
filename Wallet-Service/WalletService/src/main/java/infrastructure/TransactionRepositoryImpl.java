package infrastructure;

import domain.model.Transaction;
import exception.DuplicateTransactionIdException;

import java.util.HashSet;
import java.util.Set;

public class TransactionRepositoryImpl implements TransactionRepository {
    private final Set<Transaction> allTransaction = new HashSet<Transaction>();

    @Override
    public void saveTransaction(Transaction transaction) throws DuplicateTransactionIdException {
        if (!allTransaction.contains(transaction)) {
            allTransaction.add(transaction);
        } else {
            throw new DuplicateTransactionIdException("ERROR: DUPLICATE TRANSACTION ID. OPERATION TERMINATED!");
        }
    }
}
