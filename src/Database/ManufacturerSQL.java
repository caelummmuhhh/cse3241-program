package Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ManufacturerSQL {
    public static void PrintAll(Connection con) {
        String sql = "SELECT * FROM MANUFACTURER";
        QueryManager.queryAndPrint(con, sql, new String[0]);
    }

    public static ArrayList<Integer> GetListOfAllIDs(Connection con) {
        String sql = "SELECT DISTINCT ManufacturerID FROM MANUFACTURER;";
        ArrayList<Integer> ids = new ArrayList<>();
        try {
            ResultSet rs = QueryManager.query(con, sql, new String[0]);
            while (rs.next()) {
                ids.addLast(rs.getInt(1));
            }
            rs.close();
        }
        catch (SQLException err) {
            System.out.println(err.getMessage());
            return null;
        }
        return ids;
    }
}
