package services;

import domain.model.TransactionType;
import exception.DuplicateTransactionIdException;

public interface TransactionService {

    boolean makeTransaction(String playerId, double amount, TransactionType type) throws DuplicateTransactionIdException;

}
