package DataBase;

import Packet.UserData.UserData;

import java.sql.*;
import java.util.*;

public class DataBaseUtil {//data base communicator class

    /**
     * Executes parametrized queries for DML statements.
     * @param SQLCommand  be an SQL Data Manipulation Language (DML) statement,
     * such as INSERT, UPDATE or DELETE.
     * @param params are the parameters of the SQL statement   *
     */
    public static void executeUpdate(String SQLCommand, Object ... params){
        try (Connection sqlConnection = getSQLConnection();//try with resources statement auto closes resources
             PreparedStatement statement = sqlConnection.prepareStatement(SQLCommand)) {
            specifyParams(statement, params);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Executes parametrized SELECT statements, and executes interface implementation
     * @param SQLCommand is an SQL statement.
     * @param params are the parameters of the SQL statement   *
     * @param command is an interface implementation, that takes result set as a parameter
     * @return generic parameter, defined by method user.
     */
    public static <T> T executeQuery(String SQLCommand, ResultQuery<T> command, Object... params) {
        Connection sqlConnection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            sqlConnection = getSQLConnection();//open resources
            statement = sqlConnection.prepareStatement(SQLCommand);
            specifyParams(statement, params);
            resultSet = statement.executeQuery();
            return command.searchResultSet(resultSet);//execute interface implementation, user defined
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSet(resultSet);//close resources
            closeStatement(statement);
            closeConnection(sqlConnection);
        }
        return null;
    }

    //initializes prepared statement by defining parameters
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

    private static Connection getSQLConnection() throws SQLException {//returns a connection that allows to query master data base
        String connectionURL = "jdbc:jtds:sqlserver://localhost:1433/master;instance=MSSQLSERVER";
        return DriverManager.getConnection(connectionURL, "test3", "nikolak");
    }

    //insert user name and password into table
    public static void addUserToTable(UserData userData) {
        executeUpdate("INSERT INTO UserData (username, password)" +
                    "VALUES (?,?)", userData.getUserName(), userData.getPassword());
    }

    //insert username of sudoku solver, time to solve sudoku, and how many attempts this user has took
    public static void addTimeToSudokuAttempts(String userName, double time) {
        int attemptNum = getAttemptNum(userName);
        executeUpdate("INSERT INTO SudokuAttempt (username, timeTaken, attemptNum)" +
                    "VALUES (?,?,?)", userName, time, (attemptNum+1));
    }

    //returns the number input user has solved a sudoku
    private static Integer getAttemptNum(String username) {
        return executeQuery("SELECT attemptNum FROM dbo.SudokuAttempt " +
                        "where username = ?",
                DataBaseUtil::getMaxValue, username);
    }

    //returns largest number in result set querying attempt num in SudokuAttempt table
    private static Integer getMaxValue(ResultSet resultSet) throws SQLException {
        int maxValue = 0;
        while (resultSet.next()) {
            int value = resultSet.getInt("attemptNum");
            if (value > maxValue) maxValue = value;
        }
        return maxValue;
    }

    //update fastest impostor time for user, if the input time is faster than the current time in table
    public static void updateImpostorWinTime(String userName, double time){
        boolean isNewRecord = Objects.requireNonNull(executeQuery("SELECT fastestImpostorWin FROM UserData WHERE username = ? AND fastestImpostorWin < ?" ,
                DataBaseUtil::isResultSetEmpty, userName, time));
        if (!isNewRecord)return;
        executeUpdate("UPDATE UserData SET fastestImpostorWin = ?"+
                "WHERE username = ?", time, userName);
    }

    //queries UserData table, and returns linked hash map, which maps the username to the fastest impostor win of the user.
    //map returns the 10 records with the fastest time.
    public static LinkedHashMap<String, Double> getLeaderBoard(){
        return executeQuery("SELECT username, fastestImpostorWin FROM UserData WHERE fastestImpostorWin IS NOT NULL ORDER BY fastestImpostorWin ASC ",
                DataBaseUtil::createLeaderBoard);
    }

    //returns linked hashmap representing leader board, using results set
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


    //closes connection
    private static void closeConnection(Connection connection) {
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //closes statement
    private static void closeStatement(Statement statement) {
        try {
            if (statement != null) statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //closes results set
    private static void closeResultSet(ResultSet resultSet) {
        try {
            if (resultSet != null) resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
