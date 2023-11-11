package service;

import domain.model.Transaction;
import domain.model.TransactionType;
import exception.DuplicateTransactionIdException;
import infrastructure.TransactionRepository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.UUID;

public class TransactionServiceImpl implements TransactionService {

    /**
     * Service for working with players.
     */
    private final PlayerService playerService;

    /**
     * Transaction repository for accessing transaction data.
     */
    private final TransactionRepository transactionRepository;

    /**
     * Constructor of the `TransactionServiceImpl` class that accepts a player service
     * and a transaction repository for initialization.
     * <p>
     * //@param playerService        Service for working with players.
     *
     * @param playerService
     * @param transactionRepository Transaction repository.
     */
    public TransactionServiceImpl(PlayerService playerService, TransactionRepository transactionRepository) {
        this.playerService = playerService;

        this.transactionRepository = transactionRepository;
    }

    @Override
    public boolean makeTransaction(Long playerId, BigDecimal amount, TransactionType incomingTransactionType) throws DuplicateTransactionIdException {
        try {
            String transactionCode = UUID.randomUUID().toString();
            Transaction transactionExist = transactionRepository.getTransactionByTransactionCode(transactionCode);

            if (transactionExist != null) {
                throw new DuplicateTransactionIdException("ERROR: DUPLICATE TRANSACTION ID. OPERATION TERMINATED!");
            }
            Transaction transaction = new Transaction(transactionCode, playerId, amount, incomingTransactionType);
            boolean updateBalanceSuccessful = playerService.updateBalance(transaction);
            if (updateBalanceSuccessful) {
                transactionRepository.saveTransaction(transaction);
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}