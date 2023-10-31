package service;

import domain.model.TransactionType;
import exception.DuplicateTransactionIdException;

import java.math.BigDecimal;

public interface TransactionService {

    /**
     * Performs a transaction for the specified player.
     *
     * @param playerId Player's identifier.
     * @param amount   Transaction amount.
     * @param type     Transaction type (income or expenditure).
     * @return true if the transaction is successful, otherwise false.
     * @throws DuplicateTransactionIdException If a duplicate transaction ID error occurs.
     */
    boolean makeTransaction(Long playerId, BigDecimal amount, TransactionType type) throws DuplicateTransactionIdException;

}
