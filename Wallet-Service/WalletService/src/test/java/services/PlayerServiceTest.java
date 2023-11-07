package services;

import domain.model.Player;
import domain.model.Transaction;
import domain.model.TransactionType;
import infrastructure.PlayerRepositoryImpl;
import infrastructure.TransactionRepositoryImpl;
import org.junit.Before;
import org.junit.Test;
import service.PlayerService;
import service.PlayerServiceImpl;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.*;

public class PlayerServiceTest {
    private final PlayerService service =
            new PlayerServiceImpl(new PlayerRepositoryImpl(), new TransactionRepositoryImpl());
    private Player player;
    private final static String NAME = "name";
    private final String PASSWORD = "password";
    private final Long PLAYER_ID = 1L;

    @Before
    public void setUp() {
        //player = new Player( NAME, PASSWORD);
    }

    @Test
    public void whenRegistrationUserIsSuccessfulReturnTrue() {
        boolean isSuccessful = service.performPlayerRegistration(NAME, PASSWORD);
        assertTrue(isSuccessful);
    }

    @Test
    public void whenRegisterUserThatAlreadyExistsReturnFalse() {
        registrationUser();
        boolean reRegistration = registrationUser();
        assertFalse(reRegistration);
    }

    @Test
    public void whenAuthenticateSuccessfulReturnPlayerId() {
        registrationUser();
        Long playerId = service.performPlayerAuthentication(NAME, PASSWORD);
        assertEquals(this.PLAYER_ID, playerId);
    }

    @Test
    public void whenAnUnregisteredUserLogsInReturnNull() {
        Long notRegisteredUser = service.performPlayerAuthentication(NAME, PASSWORD);
        assertNull(notRegisteredUser);
    }

    @Test
    public void getBalance() {
        registrationUser();
        BigDecimal balance = service.getBalance(PLAYER_ID);
        assertEquals(BigDecimal.ZERO, balance);
    }

    @Test
    public void getPlayer() {
        registrationUser();
        Player fromDB = service.getPlayer(PLAYER_ID);
        assertNotNull(fromDB);
        assertEquals(player, fromDB);
    }

    @Test
    public void whenGetUserNonExistReturnNull() {
        Player playerNull = service.getPlayer(PLAYER_ID);
        assertNull(playerNull);
    }

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

