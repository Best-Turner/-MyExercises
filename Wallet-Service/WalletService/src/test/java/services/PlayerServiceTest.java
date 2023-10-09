package services;

import domain.model.Player;
import domain.model.Transaction;
import domain.model.TransactionType;
import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.*;

public class PlayerServiceTest {
    private final PlayerService service = new PlayerServiceImpl();
    private Player player;
    private String name;
    private String password;
    private String playerId;

    @Before
    public void setUp() {
        name = "name";
        password = "password";
        playerId = generateUserID(name, password);
        player = new Player(playerId, name, password);
    }

    @Test
    public void whenRegistrationUserIsSuccessfulReturnTrue() {
        boolean isSuccessful = service.performPlayerRegistration(name, password);
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
        String playerId = service.performPlayerAuthentication(name, password);
        assertEquals(this.playerId, playerId);
    }

    @Test
    public void whenAnUnregisteredUserLogsInReturnNull() {
        String notRegisteredUser = service.performPlayerAuthentication(name, password);
        assertNull(notRegisteredUser);
    }

    @Test
    public void getBalance() {
        registrationUser();
        double balance = service.getBalance(playerId);
        assertEquals(0.0, balance, 0.0);

    }

    @Test
    public void getPlayer() {
        registrationUser();
        Player fromDB = service.getPlayer(playerId);
        assertNotNull(fromDB);
        assertEquals(player, fromDB);
    }

    @Test
    public void whenGetUserNonExistReturnNull() {
        Player playerNull = service.getPlayer(playerId);
        assertNull(playerNull);
    }

    @Test
    public void getPlayerTransactionHistory() {

    }

    @Test
    public void updateBalance() {
        registrationUser();
        double currentBalance = service.getBalance(playerId);
        assertEquals(0.0, currentBalance, 0.0);
        service.updateBalance(
                new Transaction("1", playerId, 200, TransactionType.CREDIT), "CREDIT");
        assertEquals(200, 200, 0.0);
        service.updateBalance(
                new Transaction("1", playerId, 50, TransactionType.DEBIT), "DEBIT");
        assertEquals(150, 150, 0.0);
    }


    private static String generateUserID(String username, String password) {
        try {
            String data = username + password;
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(data.getBytes("UTF-8"));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.substring(0, 10);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean registrationUser() {
        return service.performPlayerRegistration(name, password);
    }
}

