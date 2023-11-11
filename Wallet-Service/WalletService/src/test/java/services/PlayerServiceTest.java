package services;

import domain.model.Player;
import domain.model.Transaction;
import domain.model.TransactionType;
import infrastructure.PlayerRepository;
import infrastructure.PlayerRepositoryImpl;
import infrastructure.TransactionRepository;
import infrastructure.TransactionRepositoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;
import service.PlayerService;
import service.PlayerServiceImpl;

import javax.print.attribute.standard.MediaSize;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PlayerServiceTest {

    @Mock
    PlayerRepository playerRepository;
    @InjectMocks
    private PlayerServiceImpl service;
    private final static String NAME = "name";
    private final String PASSWORD = "password";
    private final Long PLAYER_ID = 1L;
    private final Player player = new Player(PLAYER_ID, NAME, PASSWORD);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whenRegistrationUserIsSuccessfulReturnTrue() throws SQLException {
        when(playerRepository.exist(NAME, PASSWORD)).thenReturn(false);
        boolean isSuccessful = service.performPlayerRegistration(NAME, PASSWORD);
        verify(playerRepository, times(1)).savePlayer(NAME, PASSWORD);
        verify(playerRepository, times(1)).exist(NAME, PASSWORD);
        assertTrue(isSuccessful);
    }

    @Test
    public void whenRegisterUserThatAlreadyExistsReturnFalse() throws SQLException {
        when(playerRepository.exist(NAME, PASSWORD)).thenReturn(true);
        boolean isSuccessful = service.performPlayerRegistration(NAME, PASSWORD);
        verify(playerRepository, never()).savePlayer(NAME, PASSWORD);
        verify(playerRepository, times(1)).exist(NAME, PASSWORD);
        assertFalse(isSuccessful);
    }

    @Test
    public void whenAuthenticateSuccessfulReturnPlayerId() throws SQLException {
        when(playerRepository.exist(NAME, PASSWORD)).thenReturn(true);
        when(playerRepository.getPlayerIdByNameAndPassword(NAME, PASSWORD)).thenReturn(PLAYER_ID);
        Long playerIdActual = service.performPlayerAuthentication(NAME, PASSWORD);
        verify(playerRepository, times(1)).exist(NAME, PASSWORD);
        verify(playerRepository, times(1)).getPlayerIdByNameAndPassword(NAME, PASSWORD);
        assertEquals(PLAYER_ID, playerIdActual);
    }

        @Test
    public void whenAnUnregisteredUserLogsInReturnNull() throws SQLException {
        when(playerRepository.exist(NAME, PASSWORD)).thenReturn(false);
        Long notRegisteredUser = service.performPlayerAuthentication(NAME, PASSWORD);
        assertNull(notRegisteredUser);
    }

    @Test
    public void getBalance() throws SQLException {
        when(playerRepository.getCurrentBalance(PLAYER_ID)).thenReturn(new BigDecimal(100));
        BigDecimal actualBalance = service.getBalance(PLAYER_ID);
        verify(playerRepository, times(1)).getCurrentBalance(PLAYER_ID);
        assertEquals(new BigDecimal(100), actualBalance);
    }

    @Test
    public void getPlayer() throws SQLException {
        when(playerRepository.getPlayer(PLAYER_ID)).thenReturn(player);
        Player fromDB = service.getPlayer(PLAYER_ID);
        verify(playerRepository, times(1)).getPlayer(PLAYER_ID);
        assertNotNull(fromDB);
        assertEquals(player, fromDB);
    }

    @Test
    public void whenGetUserNonExistReturnNull() throws SQLException {
        when(playerRepository.getPlayer(PLAYER_ID)).thenReturn(null);
        Player playerNull = service.getPlayer(PLAYER_ID);
        assertNull(playerNull);
    }

    @Test
    public void getPlayerTransactionHistory() throws SQLException {
        when(playerRepository.getPlayer(PLAYER_ID)).thenReturn(player);
        Player playerFromDB = service.getPlayer(PLAYER_ID);
        verify(playerRepository,times(1)).getPlayer(PLAYER_ID);
        List<Transaction> expected = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Transaction transaction =
                    new Transaction("someTransactionCode", PLAYER_ID, new BigDecimal(100), TransactionType.CREDIT);
            expected.add(transaction);
            playerFromDB.addTransaction(transaction);
        }
        when(playerRepository.getPlayerTransactions(PLAYER_ID)).thenReturn(expected);

        assertEquals(expected, service.getPlayerTransactionHistory(PLAYER_ID));


    }

    @Test
    public void whenAddToCurrentBalanceThenBalanceShouldBeIncreaseByThisValue() throws SQLException {
        when(playerRepository.getCurrentBalance(PLAYER_ID)).thenReturn(BigDecimal.ZERO);
        BigDecimal currentBalanceBeforeChange = service.getBalance(PLAYER_ID);
        verify(playerRepository, times(1)).getCurrentBalance(PLAYER_ID);
        assertEquals(BigDecimal.ZERO, currentBalanceBeforeChange);
        boolean updateBalanceResult = service.updateBalance(
                new Transaction("transactionCodeCredit", PLAYER_ID, new BigDecimal(200), TransactionType.CREDIT));
        verify(playerRepository, times(2)).getCurrentBalance(PLAYER_ID);
        assertTrue(updateBalanceResult);
        when(playerRepository.getCurrentBalance(PLAYER_ID)).thenReturn(new BigDecimal(200));
        assertEquals(new BigDecimal(200), service.getBalance(PLAYER_ID));
        verify(playerRepository, times(3)).getCurrentBalance(PLAYER_ID);
    }

    @Test
    public void whenIncomingAmountLessThenOrEqualsZeroThenReturnFalse() throws SQLException {
        boolean updateBalanceResult = service.updateBalance(
                new Transaction("transactionCodeCredit", PLAYER_ID, BigDecimal.ZERO, TransactionType.CREDIT));
        assertFalse(updateBalanceResult);
    }

    @Test
    public void whenDecreaseBalanceByValueThenBalanceShouldDecreaseByThisValue() throws SQLException {
        when(playerRepository.getCurrentBalance(PLAYER_ID)).thenReturn(new BigDecimal(100));
        BigDecimal currentBalance = service.getBalance(PLAYER_ID);
        verify(playerRepository, times(1)).getCurrentBalance(PLAYER_ID);
        assertEquals(new BigDecimal(100), currentBalance);
        boolean transactionCodeDebit = service.updateBalance(
                new Transaction("transactionCodeDebit", PLAYER_ID, new BigDecimal(50), TransactionType.DEBIT));
        verify(playerRepository, times(2)).getCurrentBalance(PLAYER_ID);
        assertTrue(transactionCodeDebit);
    }

    @Test
    public void whenValueIsGreaterThatCurrentBalanceThenReturnFalse() throws SQLException {
        when(playerRepository.getCurrentBalance(PLAYER_ID)).thenReturn(new BigDecimal(100));
        assertEquals(new BigDecimal(100), service.getBalance(PLAYER_ID));
        boolean updateBalanceResult = service.updateBalance(
                new Transaction("transactionCodeCredit", PLAYER_ID, new BigDecimal(101), TransactionType.DEBIT));
        assertFalse(updateBalanceResult);
    }

    @Test
    public void whenWithdrawnLessThanOrEqualToZeroThenReturnFalse() throws SQLException {
        boolean updateBalanceResult = service.updateBalance(
                new Transaction("transactionCodeCredit", PLAYER_ID, new BigDecimal(-1), TransactionType.DEBIT));
        assertFalse(updateBalanceResult);
    }

    @Test
    public void whenEnterPlayerNameAndPasswordWhichInDatabaseReturnPlayerId() throws SQLException {
        when(playerRepository.getPlayerIdByNameAndPassword(NAME, PASSWORD)).thenReturn(PLAYER_ID);
        Long playerId = service.getPlayerId(NAME, PASSWORD);
        assertEquals(PLAYER_ID, playerId);
    }

    @Test
    public void whenEnterPlayerNameAndPasswordWhichAreNotInDatabaseReturnNull() throws SQLException {
        when(playerRepository.getPlayerIdByNameAndPassword(NAME, PASSWORD)).thenReturn(null);
        assertNull(service.getPlayerId(NAME, PASSWORD));
    }
}

