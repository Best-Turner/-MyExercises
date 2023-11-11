package services;

import domain.model.Transaction;
import domain.model.TransactionType;
import exception.DuplicateTransactionIdException;
import infrastructure.PlayerRepositoryImpl;
import infrastructure.TransactionRepository;
import infrastructure.TransactionRepositoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import service.PlayerService;
import service.PlayerServiceImpl;
import service.TransactionService;
import service.TransactionServiceImpl;
import util.DBConnector;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {
    private final static Long PLAYER_ID = 1L;
    private final static BigDecimal AMOUNT = new BigDecimal(100);
    private final static String TRANSACTION_CODE = "someTransactionCode";
    private final static TransactionType TYPE = TransactionType.CREDIT;
    private Transaction transaction;

    @Mock
    private PlayerService playerService;
    @Mock
    private TransactionRepository transactionRepository;
    @InjectMocks
    private TransactionServiceImpl service;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        transaction = new Transaction(TRANSACTION_CODE, PLAYER_ID, AMOUNT, TYPE);
    }

    @Test
    public void whenTransactionSuccessfulThenReturnTrue() throws SQLException, DuplicateTransactionIdException {
        when(transactionRepository.getTransactionByTransactionCode(TRANSACTION_CODE)).thenReturn(null);
        when(playerService.updateBalance(transaction)).thenReturn(true);
        boolean actual = service.makeTransaction(PLAYER_ID, AMOUNT, TransactionType.CREDIT);
        assertTrue(actual);
    }

    @Test
    public void whenPlayerBalanceIsLessThanIncomingAmountReturnFalse() throws DuplicateTransactionIdException, SQLException {
        when(transactionRepository.getTransactionByTransactionCode(TRANSACTION_CODE)).thenReturn(null);
        when(playerService.updateBalance(transaction)).thenReturn(false);
        boolean actual = service.makeTransaction(PLAYER_ID, AMOUNT, TYPE);
        assertFalse(actual);
    }

    @Test
    public void whenTransactionCodeExistsInDataBaseThenTrowException() throws SQLException {
        when(transactionRepository.getTransactionByTransactionCode(anyString())).thenReturn(transaction);
        assertThrows(DuplicateTransactionIdException.class, () -> {
            service.makeTransaction(PLAYER_ID, AMOUNT, TYPE);
        });
        verify(transactionRepository, times(1)).getTransactionByTransactionCode(anyString());
        verify(playerService, never()).updateBalance(any(Transaction.class));
        verify(transactionRepository, never()).saveTransaction(any(Transaction.class));
    }
}
