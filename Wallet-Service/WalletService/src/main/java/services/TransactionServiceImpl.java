package services;

import domain.model.Transaction;
import domain.model.TransactionType;
import exception.DuplicateTransactionIdException;
import infrastructure.TransactionRepository;

import java.util.UUID;

public class TransactionServiceImpl implements TransactionService {
    private final PlayerService playerService;
    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(PlayerService playerService, TransactionRepository transactionRepository) {
        this.playerService = playerService;
        this.transactionRepository = transactionRepository;
    }


    @Override
    public boolean makeTransaction(String playerId, double amount, TransactionType type) throws DuplicateTransactionIdException {
        String transactionId = UUID.randomUUID().toString();

        Transaction transaction = new Transaction(transactionId, playerId, amount, type);
        String incomingTransactionType = transaction.getType().name();

        transactionRepository.saveTransaction(transaction);
        if (incomingTransactionType.equals("DEBIT") || incomingTransactionType.equals("CREDIT")) {
            return playerService.updateBalance(transaction, incomingTransactionType);
        }
        return false;
    }
}
