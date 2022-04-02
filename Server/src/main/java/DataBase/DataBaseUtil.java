package DataBase;

import Packet.UserData.UserData;

import java.sql.*;
import java.util.*;

public class DataBaseUtil {
    public static void executeUpdate(String SQLCommand, Object ... params){
        try (Connection sqlConnection = getSQLConnection();
             PreparedStatement statement = sqlConnection.prepareStatement(SQLCommand)) {
            specifyParams(statement, params);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static <T> T executeQuery(String SQLCommand, ResultQuery<T> command, Object... params) {
        Connection sqlConnection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            sqlConnection = getSQLConnection();
            statement = sqlConnection.prepareStatement(SQLCommand);
            specifyParams(statement, params);
            resultSet = statement.executeQuery();
            return command.searchResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSet(resultSet);
            closeStatement(statement);
            closeConnection(sqlConnection);
        }
        return null;
    }

    private static void specifyParams(PreparedStatement statement, Object... params) throws SQLException {
        int counter = 1;
        for (Object param : params) {
            statement.setObject(counter, param);
            counter++;
        }
    }

    public static Boolean doesUserExist(UserData userData) {
        return !Objects.requireNonNull(executeQuery("SELECT username, password FROM UserData " +
                        "where username = ? " +
                        "AND password = ?",
                DataBaseUtil::isResultSetEmpty, userData.getUserName(), userData.getPassword()));
    }

    public static Boolean doesUsernameExist(String username) {
        return !Objects.requireNonNull(executeQuery("SELECT username FROM UserData " +
                        "where username = ?",
                DataBaseUtil::isResultSetEmpty, username));
    }

    public static boolean isResultSetEmpty(ResultSet resultSet) throws SQLException {
        return !resultSet.next();//return false if no more valid records in table
    }

    private static Connection getSQLConnection() throws SQLException {
        String connectionURL = "jdbc:jtds:sqlserver://localhost:1433/master;instance=MSSQLSERVER";
        return DriverManager.getConnection(connectionURL, "test3", "nikolak");
    }

    public static void addUserToTable(UserData userData) {
        executeUpdate("INSERT INTO UserData (username, password)" +
                    "VALUES (?,?)", userData.getUserName(), userData.getPassword());
//        getStatement(statement -> {
//            statement.executeUpdate("INSERT INTO [UserData] (username, password)" +
//                    "VALUES ('" + userData.getUserName() + "','" + userData.getPassword() + "')");
//        });
    }

    public static void addTimeToSudokuAttempts(String userName, double time) {
        int attemptNum = getAttemptNum(userName);
        executeUpdate("INSERT INTO SudokuAttempt (username, timeTaken, attemptNum)" +
                    "VALUES (?,?,?)", userName, time, (attemptNum+1));
    }

    private static Integer getAttemptNum(String username) {
        return executeQuery("SELECT attemptNum FROM dbo.SudokuAttempt " +
                        "where username = ?",
                DataBaseUtil::getMaxValue, username);
    }

    private static Integer getMaxValue(ResultSet resultSet) throws SQLException {
        int maxValue = 0;
        while (resultSet.next()) {
            int value = resultSet.getInt("attemptNum");
            if (value > maxValue) maxValue = value;
        }
        return maxValue;
    }

    public static void updateImpostorWinTime(String userName, double time){
        boolean isNewRecord = Objects.requireNonNull(executeQuery("SELECT fastestImpostorWin FROM UserData WHERE username = ? AND fastestImpostorWin < ?" ,
                DataBaseUtil::isResultSetEmpty, userName, time));
        if (!isNewRecord)return;
        executeUpdate("UPDATE UserData SET fastestImpostorWin = ?"+
                "WHERE username = ?", time, userName);
    }

    public static LinkedHashMap<String, Double> getLeaderBoard(){
        return executeQuery("SELECT username, fastestImpostorWin FROM UserData WHERE fastestImpostorWin IS NOT NULL ORDER BY fastestImpostorWin ASC ",
                DataBaseUtil::createLeaderBoard);
    }

    private static LinkedHashMap<String, Double> createLeaderBoard(ResultSet resultSet) throws SQLException{
            LinkedHashMap<String, Double> leaderBoard = new LinkedHashMap<>();
            for (int i = 0; i < 10; i++) {
                if (resultSet.next()) {
                    leaderBoard.put(resultSet.getString("username"), resultSet.getDouble("fastestImpostorWin"));
                }else {
                    break;
                }
            }
            return leaderBoard;
    }


    private static void closeConnection(Connection connection) {
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void closeStatement(Statement statement) {
        try {
            if (statement != null) statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void closeResultSet(ResultSet resultSet) {
        try {
            if (resultSet != null) resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
