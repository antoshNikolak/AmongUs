package DataBase;

import java.sql.SQLException;
import java.sql.Statement;

@FunctionalInterface
public interface Query {
    void excecute(Statement statment)throws SQLException;
}
