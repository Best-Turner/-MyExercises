package services;

import domain.model.TransactionType;
import exception.DuplicateTransactionIdException;
import infrastructure.PlayerRepositoryImpl;
import infrastructure.TransactionRepository;
import infrastructure.TransactionRepositoryImpl;
import org.junit.Before;
import org.junit.Test;
import service.PlayerService;
import service.PlayerServiceImpl;
import service.TransactionService;
import service.TransactionServiceImpl;

import java.math.BigDecimal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TransactionServiceTest {
    private final static String NAME = "name";
    private final static String PASSWORD = "password";
    private TransactionService service;
    private PlayerService playerService;
    private TransactionRepository repository;

    private final Long PLAYER_ID = 1L;




    @Before
    public void setUp() {
        playerService = new PlayerServiceImpl(
                new PlayerRepositoryImpl(), new TransactionRepositoryImpl());

        repository = new TransactionRepositoryImpl();
        service = new TransactionServiceImpl(playerService, repository);
        playerService.performPlayerRegistration(NAME, PASSWORD);
    }

    @Test
    public void whenTransactionSuccessfulThenReturnTrue() throws DuplicateTransactionIdException {
        boolean actual = service.makeTransaction(PLAYER_ID, new BigDecimal(100), TransactionType.CREDIT);
        assertTrue(actual);
    }

    @Test
    public void whenPlayerBalanceIsLessThanAmountReturnFalse() throws DuplicateTransactionIdException {
        boolean actual = service.makeTransaction(PLAYER_ID, new BigDecimal(100), TransactionType.DEBIT);
        assertFalse(actual);
    }
}
