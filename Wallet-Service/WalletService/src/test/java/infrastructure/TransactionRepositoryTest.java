package infrastructure;

import domain.model.Transaction;
import domain.model.TransactionType;
import exception.DuplicateTransactionIdException;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class TransactionRepositoryTest {
    private final TransactionRepository repository = new TransactionRepositoryImpl();
    private Transaction transaction;

    @Before
    public void setUp() throws Exception {
        transaction = new Transaction("1", 1L, new BigDecimal(100), TransactionType.CREDIT);

    }

    @Test
    public void saveTransaction() throws DuplicateTransactionIdException, SQLException {
        assertEquals(0, repository.getCountTransaction());
        repository.saveTransaction(transaction);
        assertEquals(1, repository.getCountTransaction());
    }

    @Test(expected = DuplicateTransactionIdException.class)
    public void whenAddTransactionWithNonUniqueId() throws DuplicateTransactionIdException, SQLException {
        assertEquals(0, repository.getCountTransaction());
        repository.saveTransaction(transaction);
        assertEquals(1, repository.getCountTransaction());
        repository.saveTransaction(transaction);

    }


//    @Test
//    public void getTransactionById() throws DuplicateTransactionIdException {
//        assertEquals(0, repository.getCountTransaction());
//        repository.saveTransaction(transaction);
//        assertEquals(transaction, repository.getTransactionById("1"));
//    }

//    @Test
//    public void whenTransactionIdNotExistThenReturnNull() throws DuplicateTransactionIdException {
//        assertEquals(0, repository.getCountTransaction());
//        repository.saveTransaction(transaction);
//        assertNull(repository.getTransactionById("2"));
//    }

//    @Test
//    public void whenTransactionByIdExistReturnTrue() throws DuplicateTransactionIdException, SQLException {
//        assertEquals(0, repository.getCountTransaction());
//        repository.saveTransaction(transaction);
//        assertTrue(repository.existById(1L));
//    }
//    @Test
//    public void whenTransactionByIdNotExistReturnFalse() throws DuplicateTransactionIdException, SQLException {
//        assertEquals(0, repository.getCountTransaction());
//        repository.saveTransaction(transaction);
//        assertFalse(repository.existById(2L));
//    }
}