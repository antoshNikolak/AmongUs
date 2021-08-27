package DataBase;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultQuery<T> {
    T searchResultSet(ResultSet resultSet) throws SQLException;
}
