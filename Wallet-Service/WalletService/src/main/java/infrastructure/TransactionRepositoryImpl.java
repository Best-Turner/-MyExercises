package infrastructure;

import domain.model.Transaction;
import domain.model.TransactionType;
import exception.DuplicateTransactionIdException;
import util.DBConnector;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the TransactionRepository interface and provides methods
 * for interacting with a collection of Transaction objects.
 */
public class TransactionRepositoryImpl implements TransactionRepository {
    private PreparedStatement preparedStatement;
    private final Connection connection = DBConnector.getConnection();


    @Override
    public void saveTransaction(Transaction transaction) throws DuplicateTransactionIdException, SQLException {

        String sqlSaveTransaction =
                "INSERT INTO model.transaction (transactionCode, playerId, amount, transactionType) VALUES (?,?,?,?)";
        preparedStatement = connection.prepareStatement(sqlSaveTransaction);
        preparedStatement.setString(1, transaction.getTransactionCode());
        preparedStatement.setLong(2, transaction.getPlayerId());
        preparedStatement.setBigDecimal(3, transaction.getAmount());
        preparedStatement.setObject(4, transaction.getType(), Types.OTHER);
        preparedStatement.executeUpdate();
    }

    @Override
    public Transaction getTransactionByTransactionCode(String transactionCode) throws SQLException {
        String sqlTransactionById = "SELECT * FROM model.transaction WHERE transactionCode=?";
        preparedStatement = connection.prepareStatement(sqlTransactionById);
        preparedStatement.setString(1, transactionCode);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            long playerId = resultSet.getLong("player_id");
            BigDecimal amount = new BigDecimal(resultSet.getString("amount"));
            TransactionType type =
                    resultSet.getString("type").equals("CREDIT") ? TransactionType.CREDIT : TransactionType.DEBIT;
            return new Transaction(transactionCode, playerId, amount, type);
        }
        return null;
    }

    @Override
    public List<Transaction> getTransactionsByPlayerId(long playerId) throws SQLException {
        preparedStatement = connection.prepareStatement("SELECT * FROM model.transaction WHERE playerId=?");
        preparedStatement.setLong(1, playerId);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Transaction> transactionList = new ArrayList<>();
        while (resultSet.next()) {
            String transactionCode = resultSet.getString("transactionCode");
            BigDecimal amount = resultSet.getBigDecimal("amount");
            TransactionType type = (TransactionType) resultSet.getObject("transactioncode");
            transactionList.add(new Transaction(transactionCode, playerId, amount, type));
        }
        return transactionList;
    }


    @Override
    public int getCountTransaction() throws SQLException {
        String sql = "SELECT COUNT(*) FROM model.transaction";
        preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.getInt(1);
    }
}
