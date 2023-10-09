package infrastructure;

import domain.model.Transaction;
import exception.DuplicateTransactionIdException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class TransactionRepositoryImpl implements TransactionRepository {
    private final Set<Transaction> allTransaction = new HashSet<>();

    @Override
    public void saveTransaction(Transaction transaction) throws DuplicateTransactionIdException {
        if (!allTransaction.contains(transaction)) {
            allTransaction.add(transaction);
        } else {
            throw new DuplicateTransactionIdException("ERROR: DUPLICATE TRANSACTION ID. OPERATION TERMINATED!");
        }
    }

    @Override
    public Transaction getTransactionById(String transactionId) {

        Optional<Transaction> transactionFromDB = allTransaction
                .stream()
                .filter(transaction -> transaction.getTransactionId().equals(transactionId))
                .findFirst();
        return transactionFromDB.orElse(null);
    }

    @Override
    public boolean existById(String transactionId) {
        Optional<Transaction> fromDB = allTransaction.stream()
                        .filter(transaction -> transaction.getTransactionId().equals(transactionId))
                        .findFirst();

        return fromDB.isPresent();
    }

    @Override
    public int getCountTransaction() {
        return allTransaction.size();
    }


}
