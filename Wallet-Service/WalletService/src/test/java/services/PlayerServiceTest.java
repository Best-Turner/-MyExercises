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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PlayerServiceTest {

    @Mock
    PlayerRepository playerRepository;
    @Mock
    TransactionRepository transactionRepository;

    @InjectMocks
    private PlayerServiceImpl service;

    //    private final PlayerService service =
//            new PlayerServiceImpl(new PlayerRepositoryImpl(connection), new TransactionRepositoryImpl());
    private Player player;
    private final static String NAME = "name";
    private final String PASSWORD = "password";
    private final Long PLAYER_ID = 1L;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        //player = new Player( NAME, PASSWORD);
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

//    @Test
//    public void whenAnUnregisteredUserLogsInReturnNull() {
//        Long notRegisteredUser = service.performPlayerAuthentication(NAME, PASSWORD);
//        assertNull(notRegisteredUser);
//    }
//
//    @Test
//    public void getBalance() {
//        registrationUser();
//        BigDecimal balance = service.getBalance(PLAYER_ID);
//        assertEquals(BigDecimal.ZERO, balance);
//    }
//
//    @Test
//    public void getPlayer() {
//        registrationUser();
//        Player fromDB = service.getPlayer(PLAYER_ID);
//        assertNotNull(fromDB);
//        assertEquals(player, fromDB);
//    }
//
//    @Test
//    public void whenGetUserNonExistReturnNull() {
//        Player playerNull = service.getPlayer(PLAYER_ID);
//        assertNull(playerNull);
//    }

//    @Test
//    public void getPlayerTransactionHistory() {
//        registrationUser();
//
//        Player fromDB = service.getPlayer(PLAYER_ID);
//        List<Transaction> expected = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            Transaction transaction =
//                    new Transaction(PLAYER_ID, new BigDecimal(100), TransactionType.CREDIT);
//            expected.add(transaction);
//            fromDB.addTransaction(transaction);
//        }
//
//        assertEquals(expected, service.getPlayerTransactionHistory(fromDB.getId()));
//
//
//    }

//    @Test
//    public void updateBalance() {
//        registrationUser();
//        BigDecimal currentBalance = service.getBalance(PLAYER_ID);
//        assertEquals(BigDecimal.ZERO, currentBalance);
//        service.updateBalance(
//                new Transaction( PLAYER_ID, new BigDecimal(200), TransactionType.CREDIT), "CREDIT");
//        assertEquals(200, 200, 0.0);
//        service.updateBalance(
//                new Transaction( PLAYER_ID, new BigDecimal(50), TransactionType.DEBIT), "DEBIT");
//        assertEquals(150, 150);
//    }


    private boolean registrationUser() {
        return service.performPlayerRegistration(NAME, PASSWORD);
    }
}

